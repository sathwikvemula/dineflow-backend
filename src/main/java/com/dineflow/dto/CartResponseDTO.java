package com.dineflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {

    private Long cartId;

    private Double totalAmount;

    private List<CartItemResponseDTO> items;
}