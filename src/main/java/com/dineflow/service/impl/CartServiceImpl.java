package com.dineflow.service.impl;
import com.dineflow.dto.*;
import com.dineflow.entity.Order;
import com.dineflow.entity.OrderItem;
import com.dineflow.enums.OrderStatus;

import com.dineflow.entity.*;
import com.dineflow.exception.MenuItemNotFoundException;
import com.dineflow.exception.UserNotFoundException;
import com.dineflow.repository.*;
import com.dineflow.service.CartService;
import com.dineflow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Override
    @Transactional
    public CartResponseDTO addItem(AddCartItemRequestDTO request) {

        User user = getCurrentUser();

        Cart cart = getOrCreateCart(user);

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() ->
                        new MenuItemNotFoundException("Menu item not found"));

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity() + request.getQuantity());

            existingItem.setPrice(
                    menuItem.getPrice() * existingItem.getQuantity());

        } else {

            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItem(menuItem)
                    .quantity(request.getQuantity())
                    .price(menuItem.getPrice() * request.getQuantity())
                    .build();

            cart.getItems().add(cartItem);
        }

        recalculateTotal(cart);

        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateQuantity(
            Long cartItemId,
            Integer quantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);

        cartItem.setPrice(
                cartItem.getMenuItem().getPrice() * quantity);

        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();

        recalculateTotal(cart);

        cartRepository.save(cart);

        return mapToResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found"));

        Cart cart = cartItem.getCart();

        cart.getItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        recalculateTotal(cart);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        cartItemRepository.deleteAll(cart.getItems());

        cart.getItems().clear();

        cart.setTotalAmount(0.0);

        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getMyCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        return mapToResponse(cart);
    }



    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private Cart getOrCreateCart(User user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart cart = Cart.builder()
                            .user(user)
                            .totalAmount(0.0)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .items(new ArrayList<>())
                            .build();

                    return cartRepository.save(cart);
                });
    }

    private void recalculateTotal(Cart cart) {

        double total = cart.getItems()
                .stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        cart.setTotalAmount(total);
    }

    private CartResponseDTO mapToResponse(Cart cart) {

        CartResponseDTO dto = new CartResponseDTO();

        dto.setCartId(cart.getId());

        dto.setTotalAmount(cart.getTotalAmount());

        List<CartItemResponseDTO> items = cart.getItems()
                .stream()
                .map(this::mapCartItemToResponse)
                .toList();

        dto.setItems(items);

        return dto;
    }

    private CartItemResponseDTO mapCartItemToResponse(
            CartItem cartItem) {

        CartItemResponseDTO dto = new CartItemResponseDTO();

        dto.setCartItemId(cartItem.getId());

        dto.setMenuItemId(cartItem.getMenuItem().getId());

        dto.setMenuItemName(cartItem.getMenuItem().getName());

        dto.setQuantity(cartItem.getQuantity());

        dto.setPrice(cartItem.getPrice());

        return dto;
    }
    @Override
    @Transactional
    public OrderResponseDTO checkout() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderRequestDTO request = new OrderRequestDTO();

        request.setCustomerName(user.getFullName());
        request.setPhoneNumber(user.getPhoneNumber());

        List<OrderItemRequestDTO> items = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            OrderItemRequestDTO dto =
                    new OrderItemRequestDTO();

            dto.setMenuItemId(
                    cartItem.getMenuItem().getId());

            dto.setQuantity(
                    cartItem.getQuantity());

            items.add(dto);
        }

        request.setItems(items);

        OrderResponseDTO response =
                orderService.placeOrder(request);

        cartItemRepository.deleteAll(cart.getItems());

        cart.getItems().clear();

        cart.setTotalAmount(0.0);

        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);

        return response;
    }
}