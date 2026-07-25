package com.dineflow.repository;

import com.dineflow.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByNameContainingIgnoreCase(
            String keyword);
}