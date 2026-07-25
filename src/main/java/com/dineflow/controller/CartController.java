package com.dineflow.controller;

import com.dineflow.dto.AddCartItemRequestDTO;
import com.dineflow.dto.CartResponseDTO;
import com.dineflow.dto.OrderResponseDTO;
import com.dineflow.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponseDTO addItem(
            @Valid @RequestBody AddCartItemRequestDTO request) {

        return cartService.addItem(request);
    }

    @PutMapping("/{cartItemId}")
    public CartResponseDTO updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {

        return cartService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    public String removeItem(
            @PathVariable Long cartItemId) {

        cartService.removeItem(cartItemId);

        return "Item removed successfully";
    }

    @DeleteMapping
    public String clearCart() {

        cartService.clearCart();

        return "Cart cleared successfully";
    }

    @GetMapping
    public CartResponseDTO getMyCart() {

        return cartService.getMyCart();
    }
    @PostMapping("/checkout")
    public OrderResponseDTO checkout() {

        return cartService.checkout();
    }
}