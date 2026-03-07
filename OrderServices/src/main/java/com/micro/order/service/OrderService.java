package com.micro.order.service;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest,String token);
    List<OrderResponse> getOrders();
    Page<OrderResponse> getOrdersPaginated(Long userId, String status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    String deleteOrder(Long id);
}
