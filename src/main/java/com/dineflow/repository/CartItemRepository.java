package com.dineflow.repository;

import com.dineflow.entity.Cart;
import com.dineflow.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    void deleteByCart(Cart cart);
}