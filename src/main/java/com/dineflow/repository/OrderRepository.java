package com.dineflow.repository;

import com.dineflow.entity.Order;
import com.dineflow.entity.User;
import com.dineflow.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    long countByStatus(OrderStatus status);
    List<Order> findByUser(User user);
    List<Order> findByCustomerNameContainingIgnoreCase(
            String customerName);
}
