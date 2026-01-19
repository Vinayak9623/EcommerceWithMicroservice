package com.micro.order.service;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest,String token);
    List<OrderResponse> getOrders();
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
    String deleteOrder(Long id);
}
