package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.services.impl.FoodServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(PathConstants.FOOD)
public class FoodController {
    private final FoodServiceImpl foodService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    public FoodController(FoodServiceImpl foodService, ObjectMapper objectMapper) {
        this.foodService = foodService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/{foodId}")
    public ResponseEntity<ApiResponse<Food>> getById(@PathVariable Long foodId){
        Food food = foodService.getById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Food>> create(
//            @ModelAttribute @Valid FoodRequest foodRequest,
            @RequestParam("foodName") String foodName,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("menuId") Long menuId,
            @RequestParam("image") MultipartFile image) throws CustomException, JsonProcessingException {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setFoodName(foodName);
        foodRequest.setDescription(description);
        foodRequest.setPrice(price);
        foodRequest.setMenuId(menuId);
//        foodRequest.setIsDeleted(isDeleted);
        logger.debug("Test add {}", objectMapper.writeValueAsString(foodRequest));
        Food food = foodService.create(foodRequest, image);
        ApiResponse<Food> response = new ApiResponse<>("201", MessageResponseConstants.CREATE_RESPONSE, food);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Food>> update(@RequestParam Long foodId,
                                                        @RequestParam("foodName") String foodName,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("price") Double price,
                                                        @RequestParam("menuId") Long menuId,
                                                        @RequestParam MultipartFile image) throws CustomException, IOException {
        FoodRequest newFood = new FoodRequest();
        newFood.setFoodName(foodName);
        newFood.setDescription(description);
        newFood.setPrice(price);
        newFood.setMenuId(menuId);
        Food food = foodService.update(foodId, newFood, image);
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

    @DeleteMapping("/hardDelete")
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
