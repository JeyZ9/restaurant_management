package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.models.Food;

import java.io.IOException;
import java.util.Optional;

public interface FoodService {
//    List<FoodResponse> getAllFood();
    Optional<Food> getFoodById(Long foodId);
//    Optional<Food> getFoodByName(String foodName);
//    List<FoodResponse> getFoodByMenuName(String menuName);
    FoodPageResponse searchFoodByKeyword(String keyword, Integer page, Integer size) throws IOException;
    Food addFood(FoodRequest food) throws CustomException;
    Food updateFood(Long foodId, FoodRequest food) throws CustomException;
    boolean softDeleteFood(Long foodId);
    boolean hardDeleteFood(Long foodId);
    boolean restoreFood(Long foodId);
}
