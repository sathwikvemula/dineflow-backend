package com.dineflow.controller;

import com.dineflow.dto.*;
import com.dineflow.enums.OrderStatus;
import com.dineflow.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDTO placeOrder(
            @Valid @RequestBody OrderRequestDTO request) {

        return orderService.placeOrder(request);
    }
    @GetMapping
    public Page<OrderResponseDTO> getAllOrders(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction) {

        return orderService.getAllOrders(
                page,
                size,
                sortBy,
                direction);
    }
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(
            @PathVariable Long id) {

        return orderService.getOrderById(id);
    }
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusDTO request) {

        return orderService
                .updateOrderStatus(id, request);
    }
    @GetMapping("/status/{status}")
    public List<OrderResponseDTO> getOrdersByStatus(
            @PathVariable OrderStatus status) {

        return orderService.getOrdersByStatus(status);
    }
    @GetMapping("/dashboard")
    public DashboardResponseDTO getDashboardStats() {

        return orderService.getDashboardStats();
    }
    @GetMapping("/my-orders")
    public List<OrderResponseDTO> getMyOrders() {

        return orderService.getMyOrders();
    }
    @GetMapping("/search")
    public List<OrderResponseDTO> searchOrders(

            @RequestParam String customer) {

        return orderService
                .searchOrders(customer);
    }

}