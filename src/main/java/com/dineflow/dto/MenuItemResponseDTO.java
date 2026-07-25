package com.dineflow.dto;

import com.dineflow.enums.Category;
import lombok.Data;

@Data
public class MenuItemResponseDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Category category;

    private boolean veg;

    private boolean available;
}