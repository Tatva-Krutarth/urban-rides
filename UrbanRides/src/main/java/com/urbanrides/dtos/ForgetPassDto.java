package com.urbanrides.dtos;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ForgetPassDto {

    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "OTP is required")
    @Pattern(regexp = "\\d{4}", message = "OTP must be a 4-digit number")
    private String otp;
    @Size(min = 8, max = 13, message = "Password must be between 8 and 20 characters")
    @NotNull(message = "Password is required")
    private String password;

    @Size(min = 8, max = 13, message = "Confirm password must be between 8 and 20 characters")
    @NotNull(message = "Confirm password is required")
    private String confPass;
}