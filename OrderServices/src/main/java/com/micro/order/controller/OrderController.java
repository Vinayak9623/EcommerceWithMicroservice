package com.micro.order.controller;

import com.micro.order.common.ApiResponse;
import com.micro.order.dto.request.OrderRequest;
import com.micro.order.dto.response.OrderResponse;
import com.micro.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {

        OrderResponse order = orderService.placeOrder(orderRequest,token);

        return ResponseEntity.ok(new ApiResponse<>(
                        201,
                        "Order created successfully",
                        order,
                        null,
                        LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrder() {

        List<OrderResponse> orders = orderService.getOrders();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Orders fetched successfully",
                        orders,
                        null,
                        LocalDateTime.now()
                )
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequest orderRequest) {

        OrderResponse order = orderService.updateOrder(id, orderRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Order updated successfully",
                        order,
                        null,
                        LocalDateTime.now()
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(
            @PathVariable Long id) {

        String message = orderService.deleteOrder(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        message,
                        message,
                        null,
                        LocalDateTime.now()
                )
        );
    }
}

