package com.micro.order.service.serviceImpl;

import com.micro.order.client.ProductClient;
import com.micro.order.client.UserClient;
import com.micro.order.dto.request.OrderItemRequest;
import com.micro.order.globalException.customException.OrderNotFoundException;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;
import com.micro.order.model.Order;
import com.micro.order.model.OrderItem;
import com.micro.order.repository.OrderRepository;
import com.micro.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper orderMapper;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Override
    public OrderResponse placeOrder(OrderRequest request, String token) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain items");
        }

        // 1️⃣ Create order with PENDING status first
        Order order = Order.builder()
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalAmount(0)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItemRequest> reducedItems = new ArrayList<>();
        double totalAmount = 0;

        try {
            // 2️⃣ Validate user
            userClient.validateUser(request.getUserId(), token);

            // 3️⃣ Reduce stock
            for (OrderItemRequest item : request.getItems()) {

                productClient.reduceStock(
                        item.getProductId(),
                        item.getQuantity(),
                        token
                );

                reducedItems.add(item);

                totalAmount += item.getPrice() * item.getQuantity();
            }

            // 4️⃣ Create order items
            List<OrderItem> orderItems = request.getItems().stream()
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

            return orderMapper.map(finalOrder, OrderResponse.class);

        } catch (Exception ex) {

            // 🔁 Rollback stock if reduced
            for (OrderItemRequest item : reducedItems) {
                productClient.restoreStock(
                        item.getProductId(),
                        item.getQuantity(),
                        token
                );
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
    public String deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + id));

        orderRepository.delete(order);
        return "Order with id " + id + " deleted successfully";
    }
}

