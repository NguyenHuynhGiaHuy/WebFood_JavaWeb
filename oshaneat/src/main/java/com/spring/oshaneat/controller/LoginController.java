package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.SignUpRequest;
import com.spring.oshaneat.service.imp.LoginServiceImp;
import com.spring.oshaneat.utils.JwtUtilsHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginServiceImp loginServiceImp;

    @Autowired
    JwtUtilsHelper jwtUtilsHelper;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String username, @RequestParam String password) {
        ResponseData responseData = new ResponseData();
        HttpStatus status;
        if(loginServiceImp.checkLogin(username, password)){
            String token = jwtUtilsHelper.generateToken(username);
            responseData.setData(token);
            status = HttpStatus.OK;
        } else {
            responseData.setDescription("Invalid username or password");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseData, status);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        ResponseData responseData = loginServiceImp.addUser(signUpRequest);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }
}
