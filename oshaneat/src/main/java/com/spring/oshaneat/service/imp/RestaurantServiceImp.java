package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.dto.RestaurantDTO;
import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.entity.Restaurant;
import com.spring.oshaneat.payload.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantServiceImp {
    public boolean insertRestaurant(String title, String subTitle, String description, MultipartFile file, boolean isFreeship, String address, String open_date);
    List<RestaurantDTO> getHomeRestaurants();
    RestaurantDTO getDetailRestaurantById(int id);

    Page<RestaurantDTO> getAllRestaurant(String searchValue, Pageable pageable);

    RestaurantDTO getRestaurantById(int id);

    boolean updateRestaurantById(int id, String title, String subTitle, String description, MultipartFile file, boolean isFreeship, String address, String openDate);

    boolean deleteRestaurantById(int id);
}
