package com.urbanrides.dtos;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RiderUMPersonalDetailDto {
    @NotBlank(message = "Pickup Address is required")
    @Length(max = 10, message = "First-name cannot exceed 20 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 10, message = "Last-name cannot exceed 20 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 characters")
    private String phone;

}
