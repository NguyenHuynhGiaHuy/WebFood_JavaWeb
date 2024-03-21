package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.SignUpRequest;

import java.util.List;

public interface LoginServiceImp {
    boolean checkLogin(String username, String password);
    ResponseData addUser(SignUpRequest signUpRequest);
}
