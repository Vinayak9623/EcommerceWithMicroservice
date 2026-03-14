package com.micro.order.service.serviceImpl;

import com.micro.order.client.CartClient;
import com.micro.order.client.NotificationClient;
import com.micro.order.client.ProductClient;
import com.micro.order.client.UserClient;
import com.micro.order.dto.response.CartDto;
import com.micro.order.dto.response.CartItemDto;
import com.micro.order.globalException.customException.OrderNotFoundException;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;
import com.micro.order.model.Order;
import com.micro.order.model.OrderItem;
import com.micro.order.repository.OrderRepository;
import com.micro.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper orderMapper;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final CartClient cartClient;
    private final NotificationClient notificationClient;

    @Override
    public OrderResponse placeOrder(OrderRequest request, String token) {

        // 1️⃣ Parallel Fetch: Get Cart and User Details Concurrently
        CompletableFuture<CartDto> cartFuture = CompletableFuture.supplyAsync(
                () -> cartClient.getCart(request.getUserId(), token));
        
        CompletableFuture<UserClient.UserResponse> userFuture = CompletableFuture.supplyAsync(
                () -> userClient.getUserById(request.getUserId(), token));

        CartDto cart;
        UserClient.UserResponse user;
        
        try {
            CompletableFuture.allOf(cartFuture, userFuture).join();
            cart = cartFuture.get();
            user = userFuture.get();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch cart or user details", ex);
        }

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // 2️⃣ Create order with PENDING status first
        Order order = Order.builder()
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalAmount(0)
                .build();

        Order savedOrder = orderRepository.save(order);

        ConcurrentLinkedQueue<CartItemDto> successfulReductions = new ConcurrentLinkedQueue<>();

        try {
            // 3️⃣ Reduce stock in parallel
            List<CompletableFuture<Void>> reduceStockFutures = cart.getItems().stream()
                    .map(item -> CompletableFuture.runAsync(() -> {
                        productClient.reduceStock(
                                item.getProductId(),
                                item.getQuantity(),
                                token
                        );
                        successfulReductions.add(item);
                    }))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(reduceStockFutures.toArray(new CompletableFuture[0])).join();

            double totalAmount = successfulReductions.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();

            // 4️⃣ Create order items
            List<OrderItem> orderItems = cart.getItems().stream()
                    .map(item -> OrderItem.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .order(savedOrder)
                            .build()
                    ).collect(Collectors.toList());

            savedOrder.setItems(orderItems);
            savedOrder.setTotalAmount(totalAmount);
            savedOrder.setStatus("CREATED");

            Order finalOrder = orderRepository.save(savedOrder);
            
            // 5️⃣ Clear cart on success (Asynchronously)
            CompletableFuture.runAsync(() -> {
                try {
                    cartClient.clearCart(request.getUserId(), token);
                } catch (Exception ignored) {
                    // Log and ignore
                }
            });

            // 6️⃣ Send Email Notification (Asynchronously)
            CompletableFuture.runAsync(() -> {
                try {
                    NotificationClient.EmailRequest emailRequest = NotificationClient.EmailRequest.builder()
                            .toEmail(user.getEmail())
                            .customerName(user.getName())
                            .orderId(finalOrder.getId())
                            .subject("Order Confirmation - #" + finalOrder.getId())
                            .totalAmount(BigDecimal.valueOf(finalOrder.getTotalAmount()))
                            .orderStatus(finalOrder.getStatus())
                            .build();

                    notificationClient.sendOrderConfirmation(emailRequest, token);
                } catch (Exception ex) {
                    System.err.println("Failed to send email notification: " + ex.getMessage());
                }
            });

            return orderMapper.map(finalOrder, OrderResponse.class);

        } catch (Exception ex) {

            // 🔁 Rollback stock asynchronously for items that succeeded
            List<CompletableFuture<Void>> rollbackFutures = successfulReductions.stream()
                    .map(item -> CompletableFuture.runAsync(() -> 
                            productClient.restoreStock(item.getProductId(), item.getQuantity(), token)))
                    .collect(Collectors.toList());
            
            try {
                CompletableFuture.allOf(rollbackFutures.toArray(new CompletableFuture[0])).join();
            } catch (Exception rollbackEx) {
                System.err.println("Failed during rollback: " + rollbackEx.getMessage());
            }

            // ❗ Mark order as FAILED
            savedOrder.setStatus("FAILED");
            orderRepository.save(savedOrder);

            throw new RuntimeException("Order placement failed", ex);
        }
    }


    @Override
    public List<OrderResponse> getOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> orderMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getOrdersPaginated(Long userId, String status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Order> orders = orderRepository.filterOrders(userId, status, startDate, endDate, pageable);
        return orders.map(order -> orderMapper.map(order, OrderResponse.class));
    }

    @Override
    public String deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + id));

        orderRepository.delete(order);
        return "Order with id " + id + " deleted successfully";
    }
}
