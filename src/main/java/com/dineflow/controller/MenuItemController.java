package com.dineflow.controller;

import com.dineflow.dto.MenuItemRequestDTO;
import com.dineflow.dto.MenuItemResponseDTO;
import com.dineflow.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public MenuItemResponseDTO createMenuItem(
            @RequestBody MenuItemRequestDTO request
    ) {
        return menuItemService.createMenuItem(request);
    }

    @GetMapping
    public List<MenuItemResponseDTO> getAllMenuItems() {
        return menuItemService.getAllMenuItems();
    }

    @GetMapping("/{id}")
    public MenuItemResponseDTO getMenuItemById(
            @PathVariable Long id
    ) {
        return menuItemService.getMenuItemById(id);
    }

    @PutMapping("/{id}")
    public MenuItemResponseDTO updateMenuItem(
            @PathVariable Long id,
            @RequestBody MenuItemRequestDTO request
    ) {
        return menuItemService.updateMenuItem(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteMenuItem(
            @PathVariable Long id
    ) {
        menuItemService.deleteMenuItem(id);
        return "Menu item deleted successfully";
    }
    @GetMapping("/search")
    public List<MenuItemResponseDTO> searchMenuItems(

            @RequestParam String keyword) {

        return menuItemService
                .searchMenuItems(keyword);
    }
}