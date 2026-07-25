package com.dineflow.service;

import com.dineflow.dto.RestaurantRequestDTO;
import com.dineflow.dto.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDTO createRestaurant(RestaurantRequestDTO request);
    List<RestaurantResponseDTO> getAllRestaurants();
    RestaurantResponseDTO getRestaurantById(Long id);
    RestaurantResponseDTO updateRestaurant(
            Long id,
            RestaurantRequestDTO request
    );
    void deleteRestaurant(Long id);

}
