package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.entity.PasswordResetToken;
import com.spring.oshaneat.entity.Users;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.ProfileRequest;
import com.spring.oshaneat.payload.request.ResetPassRequest;
import com.spring.oshaneat.repository.TokenRepository;
import com.spring.oshaneat.repository.UserRepository;
import com.spring.oshaneat.service.imp.UserServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setCreateDate(user.getCreateDate());
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public ResponseData getUser(UserDetails user) {
        Optional<Users> usersOptional = userRepository.findByUsername(user.getUsername());
        Users users = usersOptional.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(users.getId());
        userDTO.setUsername(users.getUsername());
        userDTO.setFullName(users.getFullName());
        userDTO.setCreateDate(users.getCreateDate());
        userDTO.setAddress(users.getAddress());
        userDTO.setPhone(users.getPhone());
        return new ResponseData("success", userDTO, HttpStatus.OK);
    }


    @Override
    public ResponseData updateUser(UserDetails user, ProfileRequest info) {
        if (user == null) {
            // Xử lý khi đối tượng UserDetails là null
            return new ResponseData("User details not available", null, HttpStatus.BAD_REQUEST);
        }

        Optional<Users> usersOptional = userRepository.findByUsername(user.getUsername());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            users.setUsername(info.getUsername());
            users.setFullName(info.getFullName());
            users.setPhone(info.getPhone());
            users.setAddress(info.getAddress());

            userRepository.save(users);
            return new ResponseData("success", null, HttpStatus.OK);
        } else {
            return new ResponseData("User not found", null, HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public ResponseData updateAddress(UserDetails user, String address) {
        Optional<Users> usersOptional = userRepository.findByUsername(user.getUsername());
        Users users = usersOptional.get();
        users.setAddress(address);

        userRepository.save(users);
        return new ResponseData("success", null, HttpStatus.OK);
    }

    @Override
    public ResponseData sendEmail(String email) {
        ResponseData responseData = new ResponseData();

        try {
            Optional<Users> user = userRepository.findByUsername(email);
            if (user.isEmpty()) {
                responseData.setDescription("User not found with this email");
                responseData.setHttpStatus(HttpStatus.NOT_FOUND);
                return responseData;
            }

            String token = generateResetToken(user.get());
            String resetLink = "http://127.0.0.1:5500/reset-password.html?token=" + token;

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("Osahaneat");
            msg.setTo(email);

            msg.setSubject("Password Reset Request");
            msg.setText("Hello \n\n" + "Please click on this link to reset your password: " + resetLink + "\n\n"
                    + "Regards,\nOsahaneat");

            javaMailSender.send(msg);

            responseData.setDescription("Reset password link has been sent to " + email);
            responseData.setHttpStatus(HttpStatus.OK);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setDescription("Error sending reset password email");
            responseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return responseData;
        }
    }

    @Override
    public ResponseData passwordReset(ResetPassRequest request) {
        ResponseData responseData = new ResponseData();

        // Tìm token trong cơ sở dữ liệu
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken());
        if (resetToken == null) {
            responseData.setDescription("Invalid reset token");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
            return responseData;
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            responseData.setDescription("Password and confirm password do not match");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
            return responseData;
        }

        Users user = resetToken.getUser();
        user.setPass(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Xóa token sau khi sử dụng
        tokenRepository.delete(resetToken);

        responseData.setDescription("Password has been reset successfully");
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    private String generateResetToken(Users user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(5);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDateTime(expiryDateTime);

        tokenRepository.save(resetToken);

        return token;
    }
    // xóa user theo id
    @Override
    public boolean deleteUserById(int userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            userRepository.delete(user);
            return true;
        } else {
            // Xử lý khi không tìm thấy người dùng với ID cụ thể
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }
    // lấy user theo id
    @Override
    public UserDTO getUserById(int userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setCreateDate(user.getCreateDate());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhone(user.getPhone());
            return userDTO;
        } else {
            // Xử lý khi không tìm thấy người dùng với ID cụ thể
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }
}
