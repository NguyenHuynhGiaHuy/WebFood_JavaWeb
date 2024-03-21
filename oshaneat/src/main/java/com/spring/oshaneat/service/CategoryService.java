package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.CategoryDTO;
import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.entity.Category;
import com.spring.oshaneat.entity.Food;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.FilterRequest;
import com.spring.oshaneat.repository.CategoryReposity;
import com.spring.oshaneat.service.imp.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceImp {

    @Autowired
    CategoryReposity categoryReposity;

    @Override
    public List<CategoryDTO> getHomeCategories() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Category> categoryList = categoryReposity.findAll(pageRequest);
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(category.getId());
            categoryDTO.setName(category.getNameCate());

            List<FoodDTO> menuDTOList = new ArrayList<>();
            int i = 0;
            for (Food food: category.getListFoods()) {
                if(i > 3) {
                    break;
                }
                FoodDTO foodDTO = new FoodDTO();
                foodDTO.setId(food.getId());
                foodDTO.setTitle(food.getTitle());
                foodDTO.setImage(food.getImage());
                foodDTO.setFreeShip(food.isFreeShip());
                foodDTO.setDescription(food.getDescription());
                foodDTO.setPrice(food.getPrice());
                foodDTO.setRestaurant(food.getRestaurant().getTitle());
                menuDTOList.add(foodDTO);
                i++;
            }
            categoryDTO.setFoodDTOList(menuDTOList);

            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public ResponseData getAllFoodInCategory(int categoryId) {
        ResponseData responseData = new ResponseData();
        Optional<Category> categoryOptional = categoryReposity.findById(categoryId);

        if (categoryOptional.isEmpty()) {
            responseData.setDescription("Category not found");
            responseData.setHttpStatus(HttpStatus.NOT_FOUND);
            return responseData;
        }

        Category category = categoryOptional.get();
        List<FoodDTO> foodDTOList = new ArrayList<>();
        for (Food food: category.getListFoods()) {
            FoodDTO dto = new FoodDTO();
            dto.setId(food.getId());
            dto.setTitle(food.getTitle());
            dto.setImage(food.getImage());
            dto.setFreeShip(food.isFreeShip());
            dto.setDescription(food.getDescription());
            dto.setPrice(food.getPrice());
            dto.setRestaurant(food.getRestaurant().getTitle());

            foodDTOList.add(dto);
        }

        responseData.setData(foodDTOList);
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }


    @Override
    public ResponseData filterFood(Integer categoryId, long minPrice, long maxPrice, String sortBy) {
        ResponseData responseData = new ResponseData();
        Optional<Category> categoryOptional = categoryReposity.findById(categoryId);

        if (categoryOptional.isEmpty()) {
            responseData.setDescription("Category not found");
            responseData.setHttpStatus(HttpStatus.NOT_FOUND);
            return responseData;
        }

        Category category = categoryOptional.get();
        List<Food> foods = category.getListFoods().stream()
                .filter(food -> (food.getPrice() >= minPrice &&
                        (food.getPrice() <= maxPrice)))
                .sorted(getComparator(sortBy))
                .toList();

        List<FoodDTO> foodDTOList = new ArrayList<>();
        for (Food food: foods) {
            FoodDTO dto = new FoodDTO();
            dto.setId(food.getId());
            dto.setTitle(food.getTitle());
            dto.setImage(food.getImage());
            dto.setFreeShip(food.isFreeShip());
            dto.setDescription(food.getDescription());
            dto.setPrice(food.getPrice());
            dto.setRestaurant(food.getRestaurant().getTitle());

            foodDTOList.add(dto);
        }

        responseData.setData(foodDTOList);
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    @Override
    public ResponseData getAll() {
        List<Category> categoryList = categoryReposity.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(category.getId());
            categoryDTO.setName(category.getNameCate());

            categoryDTOList.add(categoryDTO);
        }
        return new ResponseData("", categoryDTOList, HttpStatus.OK);
    }

    private Comparator<Food> getComparator(String sortBy) {
        if ("asc".equals(sortBy)) {
            return Comparator.comparing(Food::getPrice);
        } else if ("desc".equals(sortBy)) {
            return Comparator.comparing(Food::getPrice).reversed();
        }
        return Comparator.comparing(Food::getId);
    }
}
