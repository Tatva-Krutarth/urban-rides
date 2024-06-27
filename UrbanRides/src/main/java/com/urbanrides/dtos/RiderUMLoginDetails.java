package com.urbanrides.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RiderUMLoginDetails {
    @Size(min = 8, max = 13, message = "Password must be between 8 and 20 characters")
    @NotNull(message = "Password is required")
    private String currentPassword;
    @Size(min = 8, max = 13, message = "Password must be between 8 and 20 characters")
    @NotNull(message = "Password is required")
    private String newPassword;
    @Size(min = 8, max = 13, message = "Confirm password must be between 8 and 20 characters")
    @NotNull(message = "Confirm password is required")
    private String confirmPassword;
}
