package com.spring.oshaneat.controller;

import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.ProfileRequest;
import com.spring.oshaneat.service.UserService;
import com.spring.oshaneat.service.imp.UserServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    private UserServiceImp userServiceimp;
    // Lấy tất cả người dùng
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseData> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileRequest info) {
        ResponseData responseData = userService.updateUser(userDetails, info);

        if ("success".equals(responseData.getHttpStatus())) {
            return new ResponseEntity<>(new ResponseData("User updated successfully", null, HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseData, responseData.getHttpStatus().equals("User not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        try {
            boolean deleted = userService.deleteUserById(userId);
            if (deleted) {
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error deleting user: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        try {
            UserDTO userDTO = userService.getUserById(userId);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error retrieving user: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
//    // Lấy thông tin của một người dùng dựa trên ID
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") int userId) {
//        UserDTO user = userService.getUserById(userId);
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
    // Cập nhật thông tin của một người dùng

//
//


