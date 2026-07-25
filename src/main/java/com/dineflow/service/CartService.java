package com.dineflow.service;

import com.dineflow.dto.AddCartItemRequestDTO;
import com.dineflow.dto.CartResponseDTO;
import com.dineflow.dto.OrderResponseDTO;

public interface CartService {

    CartResponseDTO addItem(AddCartItemRequestDTO request);

    CartResponseDTO updateQuantity(
            Long cartItemId,
            Integer quantity);

    void removeItem(Long cartItemId);

    void clearCart();

    CartResponseDTO getMyCart();
    OrderResponseDTO checkout();
}