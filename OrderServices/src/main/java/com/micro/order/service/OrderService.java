package com.micro.order.service;
import com.micro.order.dto.OrderDetailsDto;
import com.micro.order.dto.OrderDto;
import com.micro.order.externalServices.OrderRequest;

import java.util.List;

public interface OrderService {

    public OrderDto placeOrder(OrderRequest orderRequest);
    List<OrderDto> getOrders();
    OrderDto updateOrder(Long id,OrderDto orderDto);
    String deleteOrder(Long id);
    List<OrderDto> getOrderHistory(Long userId);
    OrderDetailsDto getOrderDetails(Long id);
    List<OrderDto> getOrderByUserId(Long userId);
}
