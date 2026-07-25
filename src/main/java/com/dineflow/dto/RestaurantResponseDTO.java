package com.dineflow.dto;

import lombok.Data;

@Data
public class RestaurantResponseDTO {

    private Long id;

    private String restaurantName;

    private String phoneNumber;

    private String address;

    private String description;

    private boolean isOpen;
}