package com.micro.order.service.serviceImpl;

import com.micro.order.client.ProductClient;
import com.micro.order.globalException.customException.OrderNotFoundException;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;
import com.micro.order.model.Order;
import com.micro.order.repository.OrderRepository;
import com.micro.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper orderMapper;
    private final ProductClient productClient;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest, String token) {

        productClient
                .reduceStock(orderRequest.getProductId()
                        , orderRequest.getQuantity(), token);

        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .price(orderRequest.getPrice())
                .orderDate(LocalDateTime.now())
                .status("CREATED")
                .build();

        Order savedOrder = orderRepository.save(order);
        return orderMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> orderMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + id));


        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(orderRequest.getPrice());
        order.setStatus("UPDATED");

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.map(updatedOrder, OrderResponse.class);
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

