package com.dineflow.service.impl;

import com.dineflow.dto.RestaurantRequestDTO;
import com.dineflow.dto.RestaurantResponseDTO;
import com.dineflow.entity.Restaurant;
import com.dineflow.repository.RestaurantRepository;
import com.dineflow.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponseDTO createRestaurant(
            RestaurantRequestDTO request
    ) {

        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantName(request.getRestaurantName());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setAddress(request.getAddress());
        restaurant.setDescription(request.getDescription());

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurant);

        RestaurantResponseDTO response =
                new RestaurantResponseDTO();

        response.setId(savedRestaurant.getId());
        response.setRestaurantName(savedRestaurant.getRestaurantName());
        response.setPhoneNumber(savedRestaurant.getPhoneNumber());
        response.setAddress(savedRestaurant.getAddress());
        response.setDescription(savedRestaurant.getDescription());
        response.setOpen(savedRestaurant.isOpen());

        return response;
    }

    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {

        List<Restaurant> restaurants =
                restaurantRepository.findAll();

        return restaurants.stream()
                .map(this::mapToResponse)
                .toList();
    }
    private RestaurantResponseDTO mapToResponse(
            Restaurant restaurant
    ) {

        RestaurantResponseDTO dto =
                new RestaurantResponseDTO();

        dto.setId(restaurant.getId());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
        dto.setAddress(restaurant.getAddress());
        dto.setDescription(restaurant.getDescription());
        dto.setOpen(restaurant.isOpen());

        return dto;
    }
    @Override
    public RestaurantResponseDTO getRestaurantById(Long id){
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->new RuntimeException("Restaurant not found with id" + id));
        return mapToResponse(restaurant);
    }
    @Override
    public RestaurantResponseDTO updateRestaurant(
            Long id,
            RestaurantRequestDTO request
    ){
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Restaurant not found with id" + id));
        restaurant.setRestaurantName(request.getRestaurantName());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setAddress(request.getAddress());
        restaurant.setDescription(request.getDescription());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return mapToResponse(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
       Restaurant restautant =  restaurantRepository.findById(id)
                                    .orElseThrow(()-> new RuntimeException("Restaurant not found with id" + id));
       restaurantRepository.delete(restautant);
    }
}