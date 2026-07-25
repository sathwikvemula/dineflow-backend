package com.dineflow.dto;

import com.dineflow.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MenuItemRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    @Positive(message = "Price must be greater than zero")
    private Double price;
    @NotNull(message = "Category is required")
    private Category category;

    private boolean veg;

    private boolean available;
}