package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.dto.response.food.FoodResponse;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.services.impl.FoodServiceImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<Food>> getFoodById(@PathVariable Long foodId){
        Food food = foodService.getFoodById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        ApiResponse<Food> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, food);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<FoodPageResponse>> searchFood(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) throws IOException {
        FoodPageResponse foods = foodService.searchFoodByKeyword(keyword, page, size);
        ApiResponse<FoodPageResponse> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, foods);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Food>> addFood(@Valid @RequestBody FoodRequest foodRequest) throws CustomException {
        Food food = foodService.addFood(foodRequest);
        ApiResponse<Food> response = new ApiResponse<>("201", MessageResponseConstants.CREATE_RESPONSE, food);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Food>> updateFood(@RequestParam Long foodId, @Valid @RequestBody FoodRequest newFood) throws CustomException {
        Food food = foodService.updateFood(foodId, newFood);
        ApiResponse<Food> response = new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), MessageResponseConstants.UPDATE_RESPONSE, food);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/softDelete")
    public ResponseEntity<?> softDelete(@RequestParam Long foodId) throws CustomException {
        ApiResponse<Object> response;
        boolean remove = foodService.softDeleteFood(foodId);
        if(remove){
            response = new ApiResponse<>("200", MessageResponseConstants.DELETE_RESPONSE, remove);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = new ApiResponse<>(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageResponseConstants.CAN_NOT_DELETE_RESPONSE, remove);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/hardDelete")
    public ResponseEntity<?> hardDelete(@RequestParam Long foodId) throws CustomException {
        ApiResponse<Object> response;
        boolean delete = foodService.hardDeleteFood(foodId);
        if (delete){
            response = new ApiResponse<>("200", MessageResponseConstants.HEARD_DELETE_RESPONSE, delete);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = new ApiResponse<>(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageResponseConstants.CAN_NOT_DELETE_RESPONSE, delete);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse<Object>> restore(@RequestParam Long foodId) throws CustomException {
        ApiResponse<Object> response;
        boolean restore = foodService.restoreFood(foodId);
        if(restore){
            response = new ApiResponse<>("200", MessageResponseConstants.RESTORE_RESPONSE, restore);
            return ResponseEntity.ok(response);
        }
        response = new ApiResponse<>(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageResponseConstants.CAN_NOT_RESTORE_RESPONSE, restore);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
