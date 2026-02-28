package com.micro.order.service;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest,String token);
    List<OrderResponse> getOrders();
    String deleteOrder(Long id);
}
