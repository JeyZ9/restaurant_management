package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.dto.response.food.FoodResponse;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.repository.FoodRepository;
import com.app.restaurant_management.repository.MenuRepository;
import com.app.restaurant_management.services.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final ModelMapper modelMapper;
    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, ModelMapper modelMapper, MenuRepository menuRepository){
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
        this.menuRepository = menuRepository;
    }



    @Override
    public Optional<Food> getFoodById(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("food", "id", String.valueOf(foodId)));
        return Optional.of(findFood);
    }

//    @Override
//    public Optional<Food> getFoodByName(String foodName) {
//        Food findFood = foodRepository.findFoodByFoodName(foodName).orElseThrow(() -> new ResourceNotFoundException("food", "name", foodName));
//        return Optional.of(findFood);
//    }

//    @Override
//    public List<FoodResponse> getFoodByMenuName(String menuName) {
//        List<Food> findFood = foodRepository.findByMenu_MenuName(menuName);
//        return mapToFoodResponse(findFood);
//    }

    @Override
    public FoodPageResponse searchFoodByKeyword(String keyword, Integer page, Integer size) throws IOException {
        try {
            List<FoodResponse> foods = new ArrayList<>();
            foods = findAllFoods();
            if (!keyword.isEmpty() && !keyword.trim().isEmpty()) {
                foods = foods.stream().filter(food -> food.getFoodName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
            }

            Pageable pageable = PageRequest.of(page, size);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), foods.size());
            int total = foods.size();

            List<FoodResponse> paginatedList = foods.subList(start, end);
            Page<FoodResponse> foodPage = new PageImpl<>(paginatedList, pageable, foods.size());

            FoodPageResponse foodPageRes = new FoodPageResponse();
            foodPageRes.setFoodResponses(foodPage.getContent());
            foodPageRes.setPage(page);
            foodPageRes.setSize(size);
            foodPageRes.setTotal(total);

            return foodPageRes;
        }catch(Exception err) {
            throw new IOException("Error while searching for foods", err);
        }
    }

    @Override
    public Food addFood(FoodRequest food) {
        Food mapFood = mapToEntity(food);
        return foodRepository.save(mapFood);
    }

    @Override
    public Food updateFood(Long foodId, FoodRequest food) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        findFood.setFoodName(food.getFoodName());
        findFood.setDescription(food.getDescription());
        findFood.setPrice(food.getPrice());

        Menu findMenu = menuRepository.findById(food.getMenu()).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(food.getMenu())));
        findFood.setMenu(findMenu);

        findFood.setIsDeleted(food.getIsDeleted());

        return foodRepository.save(findFood);
    }

    @Override
    public boolean softDeleteFood(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        if(!findFood.getIsDeleted()){
            findFood.setIsDeleted(true);
            foodRepository.save(findFood);
            return true;
        }

        return false;
    }

    @Override
    public boolean hardDeleteFood(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        if(findFood.getIsDeleted()){
            foodRepository.delete(findFood);
            return true;
        }

        return false;
    }

    @Override
    public boolean restoreFood(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
        if(findFood.getIsDeleted()){
            findFood.setIsDeleted(false);
            foodRepository.save(findFood);
            return true;
        }

        return false;
    }

    private List<FoodResponse> mapToFoodResponse(List<Food> foods){
        return foods.stream()
                .map(food -> modelMapper.map(food, FoodResponse.class))
                .collect(Collectors.toList());
    }

    private Food mapToEntity(FoodRequest foodRequest){
        return modelMapper.map(foodRequest, Food.class);
    }

    public List<FoodResponse> findAllFoods() {
        List<Food> foods = foodRepository.findAll();
        return mapToFoodResponse(foods);
    }


}
