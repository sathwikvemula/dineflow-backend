
package com.dineflow.service.impl;

import com.dineflow.dto.*;
import com.dineflow.entity.MenuItem;
import com.dineflow.entity.Order;
import com.dineflow.entity.OrderItem;
import com.dineflow.entity.User;
import com.dineflow.enums.OrderStatus;
import com.dineflow.exception.MenuItemNotFoundException;
import com.dineflow.exception.OrderNotFoundException;
import com.dineflow.exception.UserNotFoundException;
import com.dineflow.repository.MenuItemRepository;
import com.dineflow.repository.OrderRepository;
import com.dineflow.repository.UserRepository;
import com.dineflow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDTO placeOrder(
            OrderRequestDTO request) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"));

        Order order = new Order();

        order.setUser(user);
        order.setCustomerName(request.getCustomerName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;

        List<OrderItem> orderItems =
                new ArrayList<>();

        for (OrderItemRequestDTO itemRequest :
                request.getItems()) {

            MenuItem menuItem =
                    menuItemRepository.findById(
                                    itemRequest.getMenuItemId())
                            .orElseThrow(() ->
                                    new MenuItemNotFoundException(
                                            "Menu item not found"));

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(
                    itemRequest.getQuantity());

            double itemPrice =
                    menuItem.getPrice()
                            * itemRequest.getQuantity();

            orderItem.setPrice(itemPrice);

            totalAmount += itemPrice;

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder =
                orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDTO getOrderById(
            Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        "Order not found with id: "
                                                + id));

        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(
            Long orderId,
            UpdateOrderStatusDTO request) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        "Order not found with id: "
                                                + orderId));

        order.setStatus(request.getStatus());

        Order updatedOrder =
                orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDTO> getOrdersByStatus(
            OrderStatus status) {

        return orderRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DashboardResponseDTO getDashboardStats() {

        DashboardResponseDTO dto =
                new DashboardResponseDTO();

        dto.setTotalOrders(
                orderRepository.count());

        dto.setPendingOrders(
                orderRepository.countByStatus(
                        OrderStatus.PENDING));

        dto.setAcceptedOrders(
                orderRepository.countByStatus(
                        OrderStatus.ACCEPTED));

        dto.setDeliveredOrders(
                orderRepository.countByStatus(
                        OrderStatus.COMPLETED));

        double revenue =
                orderRepository.findAll()
                        .stream()
                        .filter(order ->
                                order.getStatus()
                                        == OrderStatus.COMPLETED)
                        .mapToDouble(
                                Order::getTotalAmount)
                        .sum();

        dto.setTotalRevenue(revenue);

        return dto;
    }

    private OrderResponseDTO mapToResponse(
            Order order) {

        OrderResponseDTO response =
                new OrderResponseDTO();

        response.setId(order.getId());
        response.setCustomerName(
                order.getCustomerName());
        response.setPhoneNumber(
                order.getPhoneNumber());
        response.setTotalAmount(
                order.getTotalAmount());
        response.setStatus(
                order.getStatus());

        List<OrderItemResponseDTO> itemResponses =
                order.getItems()
                        .stream()
                        .map(this::mapOrderItemToResponse)
                        .toList();

        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponseDTO mapOrderItemToResponse(
            OrderItem orderItem) {

        OrderItemResponseDTO dto =
                new OrderItemResponseDTO();

        dto.setMenuItemId(
                orderItem.getMenuItem().getId());

        dto.setMenuItemName(
                orderItem.getMenuItem().getName());

        dto.setQuantity(
                orderItem.getQuantity());

        dto.setPrice(
                orderItem.getPrice());

        return dto;
    }
    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDTO> getMyOrders() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return orderRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }
    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDTO> searchOrders(
            String customerName) {

        return orderRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}