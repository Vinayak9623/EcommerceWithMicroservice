package com.micro.order.controller;

import com.micro.order.dto.OrderDetailsDto;
import com.micro.order.dto.OrderDto;
import com.micro.order.externalServices.OrderRequest;
import com.micro.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/place")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderRequest orderRequest) {
        OrderDto order = orderService.placeOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


    @GetMapping("/")
    public ResponseEntity<List<OrderDto>> getAllOrder() {
        List<OrderDto> orders = orderService.getOrders();

        return ResponseEntity.ok(orders);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        OrderDto order = orderService.updateOrder(id, orderDto);

        return ResponseEntity.ok(order);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        String deleteOrder = orderService.deleteOrder(id);

        return ResponseEntity.ok(deleteOrder);
    }


    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderHistory(@PathVariable Long userId) {
        List<OrderDto> orderHistory = orderService.getOrderHistory(userId);

        return ResponseEntity.ok(orderHistory);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> order = orderService.getOrderByUserId(userId);

        return ResponseEntity.ok(order);

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> getOrderById(@PathVariable Long id) {
        OrderDetailsDto orderDetails = orderService.getOrderDetails(id);

        return ResponseEntity.ok(orderDetails);
    }
}
