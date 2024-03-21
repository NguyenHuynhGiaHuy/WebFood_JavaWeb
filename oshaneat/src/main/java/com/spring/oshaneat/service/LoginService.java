package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.entity.Roles;
import com.spring.oshaneat.entity.Users;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.SignUpRequest;
import com.spring.oshaneat.repository.UserRepository;
import com.spring.oshaneat.service.imp.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService implements LoginServiceImp {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean checkLogin(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResponseData addUser(SignUpRequest signUpRequest) {
        ResponseData responseData = new ResponseData();

        if(userRepository.existsByUsername(signUpRequest.getEmail())) {
            responseData.setDescription("Email already exists");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else if (!signUpRequest.getPassword().equals(signUpRequest.getRepassword())) {
            responseData.setDescription("Passwords do not match");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else {
            Users user = new Users();
            Roles role = new Roles();
            role.setId(2);

            user.setFullName(signUpRequest.getFullName());
            user.setUsername(signUpRequest.getEmail());
            user.setPass(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setPhone(signUpRequest.getPhone());
            user.setRoles(role);
            user.setCreateDate(new Date());
            userRepository.save(user);

            responseData.setHttpStatus(HttpStatus.OK);
        }

        return responseData;
    }
}
