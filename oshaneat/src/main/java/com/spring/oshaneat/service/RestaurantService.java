package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.CategoryDTO;
import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.dto.RestaurantDTO;
import com.spring.oshaneat.entity.*;
import com.spring.oshaneat.repository.RestaurantRepository;
import com.spring.oshaneat.service.imp.FileServiceImp;
import com.spring.oshaneat.service.imp.RestaurantServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RestaurantService implements RestaurantServiceImp {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    FileServiceImp fileServiceImp;

    @Override
    public boolean insertRestaurant(String title, String subTitle, String description, MultipartFile file, boolean isFreeship, String address, String openDate) {
        boolean isSuccess = false;
        try {
            boolean isSaveFileSuccess = fileServiceImp.saveFile(file);
            if(isSaveFileSuccess) {
                Restaurant restaurant = new Restaurant();
                restaurant.setTitle(title);
                restaurant.setSubtitle(subTitle);
                restaurant.setDescription(description);
                restaurant.setAddress(address);
                restaurant.setFreeShip(isFreeship);
                restaurant.setImage(file.getOriginalFilename());
                restaurant.setOpenDate(openDate);
                restaurantRepository.save(restaurant);
                isSuccess = true;
            }
        } catch (Exception e) {
            System.out.println("Error inserting restaurant " + e.getMessage());
        }
        return isSuccess;
    }

    @Override
    public List<RestaurantDTO> getHomeRestaurants() {
        PageRequest pageRequest = PageRequest.of(0, 6);
        Page<Restaurant> listPages = restaurantRepository.findAll(pageRequest);

        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        for (Restaurant restaurant: listPages) {
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setId(restaurant.getId());
            restaurantDTO.setTitle(restaurant.getTitle());
            restaurantDTO.setSubtitle(restaurant.getSubtitle());
            restaurantDTO.setFreeship(restaurant.isFreeShip());
            restaurantDTO.setImage(restaurant.getImage());
            restaurantDTO.setRating(calculateRating(restaurant.getListRatingRestaurants()));
            restaurantDTO.setCountCustomers(restaurant.getListRatingRestaurants().size());
            restaurantDTOList.add(restaurantDTO);
        }
        return restaurantDTOList;
    }

    private double calculateRating(Set<RatingRestaurant> ratingRestaurants) {
        double totalPoint = 0;
        for(RatingRestaurant r : ratingRestaurants) {
            totalPoint += r.getRatePoint();
        }
        return totalPoint / ratingRestaurants.size();
    }

    @Override
    public RestaurantDTO getDetailRestaurantById(int id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        if(restaurant.isPresent()) {
            List<CategoryDTO> categoryDTOList = new ArrayList<>();
            Restaurant data = restaurant.get();

            restaurantDTO.setTitle(data.getTitle());
            restaurantDTO.setSubtitle(data.getSubtitle());
            restaurantDTO.setDescription(data.getDescription());
            restaurantDTO.setImage(data.getImage());
            restaurantDTO.setFreeship(data.isFreeShip());
            restaurantDTO.setAddress(data.getAddress());
            restaurantDTO.setOpenDate(data.getOpenDate());
            restaurantDTO.setRating(calculateRating(data.getListRatingRestaurants()));
            restaurantDTO.setCountCustomers(data.getListRatingRestaurants().size());

            //category
            for (MenuRestaurant menuRestaurant : data.getListMenuRestaurants()) {

                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setName(menuRestaurant.getCategory().getNameCate());

                //menu
                List<FoodDTO> foodDTOList = new ArrayList<>();
                for (Food food : menuRestaurant.getCategory().getListFoods()) {
                    if(food.getRestaurant().getId() == restaurant.get().getId()) {
                        FoodDTO foodDTO = new FoodDTO();
                        foodDTO.setId(food.getId());
                        foodDTO.setTitle(food.getTitle());
                        foodDTO.setImage(food.getImage());
                        foodDTO.setFreeShip(food.isFreeShip());
                        foodDTO.setDescription(food.getDescription());
                        foodDTO.setPrice(food.getPrice());

                        foodDTOList.add(foodDTO);
                    }
                }

                categoryDTO.setFoodDTOList(foodDTOList);
                categoryDTOList.add(categoryDTO);
            }
            restaurantDTO.setCategories(categoryDTOList);
        }
        return restaurantDTO;
    }
    @Override
    public Page<RestaurantDTO> getAllRestaurant(String searchValue, Pageable pageable) {
        Page<Restaurant> restaurantPage;

        if (searchValue == null || searchValue.trim().isEmpty()) {
            restaurantPage = restaurantRepository.findAll(pageable);
        } else {
            restaurantPage = restaurantRepository.findBySearchValue(searchValue, pageable);
        }

        return restaurantPage.map(restaurant -> {
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setId(restaurant.getId());
            restaurantDTO.setTitle(restaurant.getTitle());
            restaurantDTO.setSubtitle(restaurant.getSubtitle());
            restaurantDTO.setDescription(restaurant.getDescription());
            restaurantDTO.setImage(restaurant.getImage());
            restaurantDTO.setFreeship(restaurant.isFreeShip());
            restaurantDTO.setOpenDate(restaurant.getOpenDate());
            restaurantDTO.setAddress(restaurant.getAddress());
            return restaurantDTO;
        });
    }
    @Override
    public RestaurantDTO getRestaurantById(int id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setId(restaurant.getId());
            restaurantDTO.setTitle(restaurant.getTitle());
            restaurantDTO.setSubtitle(restaurant.getSubtitle());
            restaurantDTO.setDescription(restaurant.getDescription());
            restaurantDTO.setImage(restaurant.getImage());
            restaurantDTO.setFreeship(restaurant.isFreeShip());
            restaurantDTO.setOpenDate(restaurant.getOpenDate());
            restaurantDTO.setAddress(restaurant.getAddress());
            // Nếu có trường categories, hãy thêm vào đây
            // restaurantDTO.setCategories(restaurant.getCategories());

            return restaurantDTO;
        } else {
            throw new EntityNotFoundException("Restaurant not found with ID: " + id);
        }
    }
    @Override
    public boolean updateRestaurantById(int id, String title, String subTitle, String description, MultipartFile file, boolean isFreeship, String address, String openDate) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();

            // Cập nhật thông tin của nhà hàng
            restaurant.setTitle(title);
            restaurant.setSubtitle(subTitle);
            restaurant.setDescription(description);
            restaurant.setFreeShip(isFreeship);
            restaurant.setAddress(address);
            restaurant.setOpenDate(openDate);
            if(file != null) {
                fileServiceImp.saveFile(file);
                restaurant.setImage(file.getOriginalFilename());
            }

            // Lưu lại những thay đổi vào cơ sở dữ liệu
            restaurantRepository.save(restaurant);
            return true;
        } else {
            // Xử lý khi không tìm thấy restaurant với ID cụ thể
            throw new EntityNotFoundException("Restaurant not found with ID: " + id);
        }
    }
    @Override
    public boolean deleteRestaurantById(int id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurantRepository.delete(restaurant);
            return true;
        } else {
            // Xử lý khi không tìm thấy restaurant với ID cụ thể
            throw new EntityNotFoundException("Restaurant not found with ID: " + id);
        }
    }

}
