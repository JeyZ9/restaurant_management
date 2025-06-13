package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.services.impl.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping(PathConstants.FOOD)
public class FoodController {
    private final FoodServiceImpl foodService;

    @Autowired
    public FoodController(FoodServiceImpl foodService) {
        this.foodService = foodService;
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<Objects>> getFood() {
//
//    }
}
