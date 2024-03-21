package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.dto.CategoryDTO;
import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.FilterRequest;

import java.util.List;

public interface CategoryServiceImp {
    public List<CategoryDTO> getHomeCategories();

    ResponseData getAllFoodInCategory(int categoryId);

    ResponseData getAll();

    ResponseData filterFood(Integer categoryId, long minPrice, long maxPrice, String sortBy);
}
