package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.models.Food;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface FoodService {
    Optional<Food> getFoodById(Long foodId);
    FoodPageResponse searchFoodByKeyword(String keyword, Integer page, Integer size) throws IOException;
    Food addFood(FoodRequest food, MultipartFile image) throws CustomException, IOException;
    Food updateFood(Long foodId, FoodRequest food, MultipartFile image) throws CustomException, IOException;
    boolean softDeleteFood(Long foodId) throws CustomException;
    boolean hardDeleteFood(Long foodId) throws CustomException;
    boolean restoreFood(Long foodId) throws CustomException;
}
