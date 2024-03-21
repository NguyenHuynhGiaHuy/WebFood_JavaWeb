package com.spring.oshaneat.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPassRequest {
    @NotBlank(message = "token is required")
    private String token;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "confirm password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String confirmPassword;
}
