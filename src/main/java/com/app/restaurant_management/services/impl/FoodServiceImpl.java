package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.FoodResponse;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.repository.FoodRepository;
import com.app.restaurant_management.services.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final ModelMapper modelMapper;
    private FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, ModelMapper modelMapper){
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<FoodResponse> getAllFood() {
        List<Food> foods = foodRepository.findAll();
        return mapToFoodResponse(foods);
    }

    @Override
    public Optional<Food> getFoodById(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("food", "id", String.valueOf(foodId)));
        return Optional.of(findFood);
    }

    @Override
    public Optional<Food> getFoodByName(String foodName) {
        Food findFood = foodRepository.findFoodByFoodName(foodName).orElseThrow(() -> new ResourceNotFoundException("food", "name", foodName));
        return Optional.of(findFood);
    }

    @Override
    public Food addFood(FoodRequest food) {
        Food mapFood = mapToEntity(food);
        return foodRepository.save(mapFood);
    }

    private List<FoodResponse> mapToFoodResponse(List<Food> foods){
        return foods.stream()
                .map(food -> modelMapper.map(food, FoodResponse.class))
                .collect(Collectors.toList());
    }

    private Food mapToEntity(FoodRequest foodRequest){
        return modelMapper.map(foodRequest, Food.class);
    }
}
