package com.dineflow.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {

    private Long cartItemId;

    private Long menuItemId;

    private String menuItemName;

    private Integer quantity;

    private Double price;
}