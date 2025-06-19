package com.app.restaurant_management.repository;

import com.app.restaurant_management.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findFoodByFoodName(String foodName);
//    List<Food> findByMenu_MenuName(String menuName);
}
