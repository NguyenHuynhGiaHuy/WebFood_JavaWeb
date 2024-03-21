package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.dto.RestaurantDTO;
import com.spring.oshaneat.entity.Category;
import com.spring.oshaneat.entity.Food;
import com.spring.oshaneat.entity.RatingRestaurant;
import com.spring.oshaneat.entity.Restaurant;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.repository.FoodRepository;
import com.spring.oshaneat.repository.RestaurantRepository;
import com.spring.oshaneat.service.imp.FileServiceImp;
import com.spring.oshaneat.service.imp.MenuServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService implements MenuServiceImp {

    @Autowired
    FileServiceImp fileServiceImp;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public boolean insertFood(String title, MultipartFile file, boolean isFreeShip, String timeShip, Long price, int cateId) {
        boolean isSuccess = false;
        try {
            boolean isSaveFileSuccess = fileServiceImp.saveFile(file);
            if(isSaveFileSuccess) {
                Food food = new Food();
                food.setTitle(timeShip);
                food.setImage(file.getOriginalFilename());
                food.setFreeShip(isFreeShip);
                food.setPrice(price);

                Category category = new Category();
                category.setId(cateId);

                food.setCategory(category);

                foodRepository.save(food);
                isSuccess = true;
            }
        } catch (Exception e) {
            System.out.println("Error inserting food " + e.getMessage());
        }
        return isSuccess;
    }

    @Override
    public ResponseData searchByKeyword(String keyword) {
        ResponseData responseData = new ResponseData();

        List<FoodDTO> foodDTOs;
        List<RestaurantDTO> restaurantDTOs;

        if (keyword == null || keyword.trim().isEmpty()) {
            List<Food> foods = foodRepository.findAll();
            List<Restaurant> restaurants = restaurantRepository.findAll();
            foodDTOs = foods.stream().map(this::convertToFoodDTO).collect(Collectors.toList());
            restaurantDTOs = restaurants.stream().map(this::convertToRestaurantDTO).collect(Collectors.toList());
        } else {

            // Tìm kiếm theo keyword
            List<Food> foods = foodRepository.findByTitleContainingIgnoreCase(keyword);
            foodDTOs = new ArrayList<>();
            restaurantDTOs = new ArrayList<>();
            if(!foods.isEmpty()) {
                for (Food food: foods) {
                    foodDTOs.add(convertToFoodDTO(food));
                    restaurantDTOs.add(convertToRestaurantDTO(food.getRestaurant()));
                }
            } else {
                List<Restaurant> restaurants = restaurantRepository.findByTitleContainingIgnoreCase(keyword);
                if(!restaurants.isEmpty()) {
                    for (Restaurant restaurant: restaurants) {
                        restaurantDTOs.add(convertToRestaurantDTO(restaurant));
                        foodDTOs = restaurant.getListFoods().stream().map(this::convertToFoodDTO).collect(Collectors.toList());
                    }
                }
            }
        }

        // Tạo và trả về kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("foods", foodDTOs);
        result.put("restaurants", restaurantDTOs);

        responseData.setData(result);
        responseData.setDescription("Search results");
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    private FoodDTO convertToFoodDTO(Food food) {
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setTitle(food.getTitle());
        dto.setImage(food.getImage());
        dto.setFreeShip(food.isFreeShip());
        dto.setDescription(food.getDescription());
        dto.setPrice(food.getPrice());
        dto.setRestaurant(food.getRestaurant().getTitle());
        return dto;
    }

    private double calculateRating(Set<RatingRestaurant> ratingRestaurants) {
        double totalPoint = 0;
        for(RatingRestaurant r : ratingRestaurants) {
            totalPoint += r.getRatePoint();
        }
        return totalPoint / ratingRestaurants.size();
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setTitle(restaurant.getTitle());
        dto.setSubtitle(restaurant.getSubtitle());
        dto.setImage(restaurant.getImage());
        dto.setRating(calculateRating(restaurant.getListRatingRestaurants()));
        dto.setFreeship(restaurant.isFreeShip());
        return dto;
    }
}
