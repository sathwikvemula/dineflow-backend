package com.dineflow.dto;

import com.dineflow.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;

    private String customerName;

    private String phoneNumber;

    private Double totalAmount;

    private OrderStatus status;

    private List<OrderItemResponseDTO> items;
}