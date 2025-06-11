package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.FoodResponse;
import com.app.restaurant_management.models.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {
    List<FoodResponse> getAllFood();
    Optional<Food> getFoodById(Long foodId);
    Optional<Food> getFoodByName(String foodName);
    Food addFood(FoodRequest food);
    Food updateFood(FoodRequest food);
    void deleteFood(Food food);
}
