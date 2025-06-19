package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.dto.response.food.FoodResponse;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.services.impl.FoodServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Object>> searchFood(
            @Valid @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) throws IOException {
        FoodPageResponse foods = foodService.searchFoodByKeyword(keyword, page, size);
        ApiResponse<Object> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, foods);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addFood")
    public ResponseEntity<ApiResponse<Food>> addFood(@Valid @RequestBody FoodRequest foodRequest) {
        Food food = foodService.addFood(foodRequest);
        ApiResponse<Food> response = new ApiResponse<>("201", MessageResponseConstants.CREATE_RESPONSE, food);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
