package com.spring.oshaneat.controller;

import com.spring.oshaneat.dto.RestaurantDTO;
import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.service.imp.FileServiceImp;
import com.spring.oshaneat.service.imp.RestaurantServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@CrossOrigin("*")
public class RestaurantController {

    @Autowired
    FileServiceImp fileServiceImp;

    @Autowired
    RestaurantServiceImp restaurantServiceImp;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> createRestaurant(@RequestParam String title,
                                              @RequestParam String subTitle,
                                              @RequestParam String description,
                                              @RequestParam MultipartFile file,
                                              @RequestParam boolean isFreeship,
                                              @RequestParam String address,
                                              @RequestParam String openDate
                                              ) {
        ResponseData responseData = new ResponseData();
//        boolean isSuccess = restaurantServiceImp.insertRestaurant(title, subTitle, description, file, isFreeship, address, openDate);
//        responseData.setData(isSuccess);
//        return new ResponseEntity<>(responseData, HttpStatus.OK);
        if (file != null && !file.isEmpty()) {
            boolean isSuccess = restaurantServiceImp.insertRestaurant(title, subTitle, description, file, isFreeship, address, openDate);
            responseData.setData(isSuccess);

            // Xử lý trạng thái HTTP tùy thuộc vào kết quả
            HttpStatus httpStatus = isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(responseData, httpStatus);
        } else {
            // Xử lý khi không có file
            responseData.setDescription("File is required");
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileRestaurant(@PathVariable String filename) {
        Resource resource = fileServiceImp.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping()
    public ResponseEntity<?> getHomeRestaurants() {
        ResponseData responseData = new ResponseData();
        responseData.setData(restaurantServiceImp.getHomeRestaurants());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetailRestaurant(@RequestParam int id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(restaurantServiceImp.getDetailRestaurantById(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/restaurants")
    public ResponseEntity<?> getAllRestaurant(@RequestParam(required = false) String search,
                                              Pageable pageable) {
        return new ResponseEntity<>(restaurantServiceImp.getAllRestaurant(search, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurantById(@PathVariable int id,
                                                  @RequestParam String title,
                                                  @RequestParam String subTitle,
                                                  @RequestParam String description,
                                                  @RequestParam(required = false) MultipartFile file,
                                                  @RequestParam boolean isFreeship,
                                                  @RequestParam String address,
                                                  @RequestParam String openDate) {
        ResponseData responseData = new ResponseData();

        try {
            boolean isSuccess = restaurantServiceImp.updateRestaurantById(id, title, subTitle, description, file, isFreeship, address, openDate);
            responseData.setData(isSuccess);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // Xử lý khi không tìm thấy restaurant với ID cụ thể
            responseData.setDescription("Restaurant not found with ID: " + id);
            return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        boolean isSuccess = restaurantServiceImp.deleteRestaurantById(id);
        responseData.setData(isSuccess);
        return new ResponseEntity<>(responseData, isSuccess ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable int id) {
        ResponseData responseData = new ResponseData();

        try {
            RestaurantDTO restaurantDTO = restaurantServiceImp.getRestaurantById(id);
            responseData.setData(restaurantDTO);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // Xử lý khi không tìm thấy restaurant với ID cụ thể
            responseData.setDescription("Restaurant not found with ID: " + id);
            return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
        }
    }

}
