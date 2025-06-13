package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.services.impl.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstants.FOOD)
public class FoodController {
    private final FoodServiceImpl foodService;

    @Autowired
    public FoodController(FoodServiceImpl foodService) {
        this.foodService = foodService;
    }

    @GetMapping(path = "/{foodId}")
    public ResponseEntity<ApiResponse<Object>> getFoodById(@PathVariable Long foodId) {
        Food food = foodService.getFoodById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        ApiResponse<Object> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, food);
        return ResponseEntity.ok(response);
    }
}
