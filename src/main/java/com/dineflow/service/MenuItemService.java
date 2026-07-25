package com.dineflow.service;

import com.dineflow.dto.MenuItemRequestDTO;
import com.dineflow.dto.MenuItemResponseDTO;

import java.util.List;

public interface MenuItemService {

    MenuItemResponseDTO createMenuItem(
            MenuItemRequestDTO request
    );

    List<MenuItemResponseDTO> getAllMenuItems();

    MenuItemResponseDTO getMenuItemById(Long id);

    MenuItemResponseDTO updateMenuItem(
            Long id,
            MenuItemRequestDTO request
    );

    void deleteMenuItem(Long id);
    List<MenuItemResponseDTO> searchMenuItems(
            String keyword);
}