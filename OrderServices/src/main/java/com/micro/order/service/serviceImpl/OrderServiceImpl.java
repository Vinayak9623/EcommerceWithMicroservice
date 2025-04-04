package com.micro.order.service.serviceImpl;

import com.micro.order.customException.OrderNotFoundException;
import com.micro.order.dto.OrderDetailsDto;
import com.micro.order.dto.OrderDto;
import com.micro.order.externalServices.*;
import com.micro.order.model.Order;
import com.micro.order.repository.OrderRepository;
import com.micro.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper orderMapper;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final HttpServletRequest request;


    @Override
    public OrderDto placeOrder(OrderRequest orderRequest) {

        String authorization = request.getHeader("Authorization");

        if(authorization==null || !authorization.startsWith("Bearer ")){

            throw new OrderNotFoundException("Invalid Token");
        }

        //UserDto user = userClient.getUserById(orderRequest.getUserId());

        UserDto user = userClient.getUserDetails(authorization);
        if(user==null){ throw new RuntimeException("User not found");}

        //ProductDto product = productClient.getProductById(orderRequest.getProductId());

        ProductDto product = productClient.getProductById(orderRequest.getProductId(),authorization);
        if(product==null){throw  new RuntimeException("Product not found");}

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderDate(LocalDate.now());

        Order saveOrder = orderRepository.save(order);

        return orderMapper.map(saveOrder, OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrders() {

        List<Order> orders = orderRepository.findAll();


        return orders.stream()
                .map(order ->
                        orderMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not Found"));

        order.setUserId(orderDto.getUserId());
        order.setProductId(orderDto.getProductId());
        order.setQuantity(orderDto.getQuantity());
        order.setOrderDate(orderDto.getOrderDate());

        Order updateOrder = orderRepository.save(order);

        return orderMapper.map(updateOrder, OrderDto.class);
    }

    @Override
    public String deleteOrder(Long id) {

        orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        orderRepository.deleteById(id);

        return "Order with Id: " + id + " deleted Sucessfully";
    }

    @Override
    public List<OrderDto> getOrderHistory(Long userId) {

        List<Order> userOrders = orderRepository.findByUserId(userId);

        return userOrders.stream().map(order -> orderMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        String authorization = request.getHeader("Authorization");

        if(authorization==null || !authorization.startsWith("Bearer ")){

            throw new OrderNotFoundException("Invalid Token");
        }
        //UserDto user = userClient.getUserById(order.getUserId());
        UserDto user = userClient.getUserDetails(authorization);
        ProductDto product = productClient.getProductById(order.getProductId(),authorization);


        return new OrderDetailsDto(
                order.getId(),
                user,
                product,
                order.getQuantity(),
                order.getOrderDate());
    }

    @Override
    public List<OrderDto> getOrderByUserId(Long userId) {

        List<Order> orderList = orderRepository.findByUserId(userId);

        return orderList.
                stream()
                .map(order->orderMapper
                        .map(order,OrderDto.class))
                .collect(Collectors.toList());
    }

}
