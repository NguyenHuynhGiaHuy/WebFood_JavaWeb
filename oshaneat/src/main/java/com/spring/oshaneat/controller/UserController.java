package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.ProfileRequest;
import com.spring.oshaneat.payload.request.ResetPassRequest;
import com.spring.oshaneat.service.imp.UserServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImp userService;

//    @GetMapping("/")
//    public ResponseEntity<?> getAllUsers() {
//        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails user) {
        ResponseData responseData = userService.getUser(user);
        return new ResponseEntity<>(responseData.getData(), responseData.getHttpStatus());
    }

    @PostMapping()
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails user, @RequestBody @Valid ProfileRequest info) {
        ResponseData responseData = userService.updateUser(user, info);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @PostMapping("/address")
    public ResponseEntity<?> updateAddress(@AuthenticationPrincipal UserDetails user,
                                           @RequestParam String address) {
        ResponseData responseData = new ResponseData();
        if(address == null || address.isEmpty()){
            responseData.setDescription("Address is required");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else {
            responseData = userService.updateAddress(user, address);
        }
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        ResponseData responseData = userService.sendEmail(email);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> passwordResetProcess(@RequestBody @Valid ResetPassRequest request) {
        ResponseData responseData = userService.passwordReset(request);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }
}
