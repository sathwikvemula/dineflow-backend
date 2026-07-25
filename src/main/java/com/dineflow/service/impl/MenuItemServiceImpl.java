package com.dineflow.service.impl;

import com.dineflow.dto.MenuItemRequestDTO;
import com.dineflow.dto.MenuItemResponseDTO;
import com.dineflow.entity.MenuItem;
import com.dineflow.exception.MenuItemNotFoundException;
import com.dineflow.repository.MenuItemRepository;
import com.dineflow.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponseDTO createMenuItem(
            MenuItemRequestDTO request
    ) {

        MenuItem menuItem = new MenuItem();

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setVeg(request.isVeg());
        menuItem.setAvailable(request.isAvailable());

        MenuItem savedMenuItem =
                menuItemRepository.save(menuItem);

        return mapToResponse(savedMenuItem);
    }

    @Override
    public List<MenuItemResponseDTO> getAllMenuItems() {

        return menuItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MenuItemResponseDTO getMenuItemById(Long id) {

        MenuItem menuItem =
                menuItemRepository.findById(id)
                        .orElseThrow(() ->
                                new MenuItemNotFoundException(
                                        "Menu item not found with id: " + id
                                ));

        return mapToResponse(menuItem);
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(
            Long id,
            MenuItemRequestDTO request
    ) {

        MenuItem menuItem =
                menuItemRepository.findById(id)
                        .orElseThrow(() ->
                                new MenuItemNotFoundException(
                                        "Menu item not found with id: " + id
                                ));

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setVeg(request.isVeg());
        menuItem.setAvailable(request.isAvailable());

        MenuItem updatedMenuItem =
                menuItemRepository.save(menuItem);

        return mapToResponse(updatedMenuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {

        MenuItem menuItem =
                menuItemRepository.findById(id)
                        .orElseThrow(() ->
                                new MenuItemNotFoundException(
                                        "Menu item not found with id: " + id
                                ));

        menuItemRepository.delete(menuItem);
    }

    private MenuItemResponseDTO mapToResponse(
            MenuItem menuItem
    ) {

        MenuItemResponseDTO dto =
                new MenuItemResponseDTO();

        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        dto.setVeg(menuItem.isVeg());
        dto.setAvailable(menuItem.isAvailable());

        return dto;
    }
    @Override
    public List<MenuItemResponseDTO> searchMenuItems(
            String keyword) {

        return menuItemRepository
                .findByNameContainingIgnoreCase(
                        keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}