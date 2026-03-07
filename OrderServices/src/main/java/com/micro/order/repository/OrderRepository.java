package com.micro.order.repository;

import com.micro.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
    @Query("SELECT o FROM Order o WHERE " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:status IS NULL OR LOWER(o.status) = LOWER(:status)) AND " +
           "(cast(:startDate as timestamp) IS NULL OR o.orderDate >= :startDate) AND " +
           "(cast(:endDate as timestamp) IS NULL OR o.orderDate <= :endDate)")
    Page<Order> filterOrders(@Param("userId") Long userId,
                             @Param("status") String status,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             Pageable pageable);
}
