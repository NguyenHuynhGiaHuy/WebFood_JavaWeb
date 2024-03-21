package com.spring.oshaneat.dto;

import com.spring.oshaneat.entity.Roles;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class UserDTO {
    private int id;
    private String username;
    private String fullName;
    private String address;
    private String phone;
    private Date createDate;
}
