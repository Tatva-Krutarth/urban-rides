package com.urbanrides.dtos;

import com.urbanrides.custom.annotations.PasswordMatches;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserLoginDto {
    @Email(message = "Email must be a valid email address")
    @NotBlank(message = "Email cannot be blank")
    @NotNull
    private String email;


    @NotNull
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 16, message = "Password must be at between 8 to 16")
    private String password;

}
