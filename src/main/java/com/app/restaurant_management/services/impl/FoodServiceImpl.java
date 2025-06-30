package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.dto.request.FoodRequest;
import com.app.restaurant_management.commons.dto.response.food.FoodPageResponse;
import com.app.restaurant_management.commons.dto.response.food.FoodResponse;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Food;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.repository.FoodRepository;
import com.app.restaurant_management.repository.MenuRepository;
import com.app.restaurant_management.services.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final ModelMapper modelMapper;
    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    @Value("${file.uploads-dir}")
    private String uploadDir;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository, ModelMapper modelMapper, MenuRepository menuRepository, ObjectMapper objectMapper){
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
    }



    @Override
    public Optional<Food> getById(Long foodId) {
        Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("food", "id", String.valueOf(foodId)));
        return Optional.of(findFood);
    }

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
    public Food create(FoodRequest food, MultipartFile image) throws CustomException {
        try {
            Food mapFood = mapToEntity(food);
            logger.debug("Add {}", objectMapper.writeValueAsString(mapFood));
            if (image != null){
                mapFood.setImage(saveFile(image));
            }else {
                mapFood.setImage("noImg.jpg");
            }
            logger.debug("Add image {}", objectMapper.writeValueAsString(mapFood));
            return foodRepository.save(mapFood);
        }catch (Exception ex){
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }

    @Override
    public Food update(Long foodId, FoodRequest food, MultipartFile image) throws CustomException, IOException {
        try {
            Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
            findFood.setFoodName(food.getFoodName());
            findFood.setDescription(food.getDescription());
            findFood.setPrice(food.getPrice());

            if(image != null){
                String newFileName = saveFile(image);
                deleteFile(findFood.getImage());
                findFood.setImage(newFileName);
            }

            Menu findMenu = menuRepository.findById(food.getMenuId()).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(food.getMenuId())));
            findFood.setMenu(findMenu);

//            findFood.setIsDeleted(food.getIsDeleted());

            return foodRepository.save(findFood);
        }catch (ResourceNotFoundException ex){
            throw ex;
        }catch (Exception ex){
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }

    @Override
    public boolean softDeleteFood(Long foodId) throws CustomException {
        try{
            Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
            findFood.setIsDeleted(true);
            foodRepository.save(findFood);
            return true;
        }catch (ResourceNotFoundException ex){
            throw ex;
        }catch (Exception ex){
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }

    }

    @Override
    public boolean hardDeleteFood(Long foodId) throws CustomException {
        try{
            Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
            foodRepository.delete(findFood);
            return true;
        }catch (ResourceNotFoundException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }

    }

    @Override
    public boolean restoreFood(Long foodId) throws CustomException {
        try {
            Food findFood = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(foodId)));
            findFood.setIsDeleted(false);
            foodRepository.save(findFood);
            return true;
        }catch (ResourceNotFoundException ex){
            throw ex;
        }catch (Exception ex){
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }

    private List<FoodResponse> mapToFoodResponse(List<Food> foods){
        return foods.stream()
                .map(food ->
                    modelMapper.map(food, FoodResponse.class)
                )
                .collect(Collectors.toList());
    }

    private Food mapToEntity(FoodRequest foodRequest){
        return modelMapper.map(foodRequest, Food.class);
    }

    public List<FoodResponse> findAllFoods() {
        List<Food> foods = foodRepository.findAll();
        return mapToFoodResponse(foods);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

//        สร้างโฟลเดอร์หากยังไม่มี
        if(!Files.exists(filePath.getParent())){
            Files.createDirectories(filePath.getParent());
        }

//        เขียนไฟล์ลงดิสก์
        try(FileOutputStream fos = new FileOutputStream(filePath.toFile())){
            fos.write(file.getBytes());
        }

        return fileName;
    }

    private void deleteFile(String fileName) throws IOException {
        if (fileName != "noImg.jpg"){
            Path filePath = Paths.get(uploadDir, fileName);
            try {
                Files.deleteIfExists(filePath);
            }catch (IOException ex){
                throw new IOException(ex.getMessage());
            }
        }
    }

}
