package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.ProfileRequest;
import com.spring.oshaneat.payload.request.ResetPassRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserServiceImp {
    List<UserDTO> getAllUsers();

    ResponseData getUser(UserDetails user);

    ResponseData updateUser(UserDetails user, ProfileRequest info);


    ResponseData updateAddress(UserDetails user, String address);


    ResponseData sendEmail(String email);

    ResponseData passwordReset(ResetPassRequest request);


    // xóa user theo id
    boolean deleteUserById(int userId);

    // lấy user theo id
    UserDTO getUserById(int userId);
}
