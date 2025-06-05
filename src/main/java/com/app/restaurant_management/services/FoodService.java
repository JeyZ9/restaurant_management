package com.app.restaurant_management.services;

import com.app.restaurant_management.models.Food;

public interface FoodService {
    Food getAllFood();
    Food getFoodById(Long id);
    Food getFoodByName(String name);
    void addFood(Food food);
    void updateFood(Food food);
    void deleteFood(Food food);
}
