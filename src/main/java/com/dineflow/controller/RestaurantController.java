package com.dineflow.controller;

import com.dineflow.dto.RestaurantRequestDTO;
import com.dineflow.dto.RestaurantResponseDTO;
import com.dineflow.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping
    public RestaurantResponseDTO createRestaurant( @Valid @RequestBody RestaurantRequestDTO request){
        return restaurantService.createRestaurant(request);
    }
    @GetMapping
    public List<RestaurantResponseDTO> getAllRestaurants(){
        return restaurantService.getAllRestaurants();
    }
    @GetMapping("/{id}")
    public RestaurantResponseDTO getRestaurantById(@PathVariable Long id){
        return restaurantService.getRestaurantById(id);
    }
    @PutMapping("/{id}")
    public RestaurantResponseDTO updateRestaurant(@PathVariable Long id,
                                                  @Valid @RequestBody RestaurantRequestDTO request) {
        return restaurantService.updateRestaurant(id, request);
    }
    @DeleteMapping("/{id}")
    public String deleteRestaurant(@PathVariable Long id){
        restaurantService.deleteRestaurant(id);
        return "Restaurant deleted successfully";
    }


}
