package com.urbanrides.dtos;

import com.urbanrides.custom.annotations.PasswordMatches;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data

public class UserRegistrationDto {
    @Email(message = "Email must be a valid email address")
    @NotBlank(message = "Email cannot be blank")
    @NotNull
    private String email;

    @Nullable
    @Size(min = 10, max = 10, message = "Phone number must be equal to 10 digits")
    private String phone;

    @Pattern(regexp = "\\d{4}", message = "OTP must be a 4-digit number")
    private String otp;

    @NotNull
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 16, message = "Password must be at between 8 to 16")
    private String password;

    @NotNull
    @NotBlank(message = "Conf Password cannot be blank")
    @Size(min = 8, max = 16, message = "Conf password must be at least 8 characters")
    private String confPass;

    @NotNull
    private int acccoutTypeId;
}

