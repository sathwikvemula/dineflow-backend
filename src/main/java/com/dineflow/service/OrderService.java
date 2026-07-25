package com.dineflow.service;

import com.dineflow.dto.*;
import com.dineflow.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO request);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO updateOrderStatus(
            Long orderId,
            UpdateOrderStatusDTO request);
    List<OrderResponseDTO> getOrdersByStatus(
            OrderStatus status);
    DashboardResponseDTO getDashboardStats();
    @Transactional(readOnly = true)
    List<OrderResponseDTO> getMyOrders();
    Page<OrderResponseDTO> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction);
    List<OrderResponseDTO> searchOrders(
            String customerName);

}
