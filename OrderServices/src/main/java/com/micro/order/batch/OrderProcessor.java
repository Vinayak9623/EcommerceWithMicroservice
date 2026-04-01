package com.micro.order.batch;

import com.micro.order.model.Order;
import org.springframework.batch.item.ItemProcessor;

public class OrderProcessor implements ItemProcessor<Order, OrderReportDTO> {
    @Override
    public OrderReportDTO process(Order order) {
        return new OrderReportDTO(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }
}
