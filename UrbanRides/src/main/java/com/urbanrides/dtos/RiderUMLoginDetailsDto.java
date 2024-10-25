package com.urbanrides.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RiderUMLoginDetailsDto {
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    @NotNull(message = "Password is required")
    private String currentPassword;
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    @NotNull(message = "Password is required")
    private String newPassword;
    @Size(min = 8, max = 16, message = "Confirm password must be between 8 and 16 characters")
    @NotNull(message = "Confirm password is required")
    private String confirmPassword;
}
