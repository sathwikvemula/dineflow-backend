package com.dineflow.dto;

import lombok.Data;

@Data
public class OrderItemResponseDTO {

    private Long menuItemId;

    private String menuItemName;

    private Integer quantity;

    private Double price;
}