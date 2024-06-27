package com.urbanrides.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class RiderPersonalDetailsDto {
//    @NotBlank(message = "First-name is required")
//    @Size(max = 50, message = "First-name cannot exceed 50 characters")
//    private String fNamee;

    @NotBlank(message = "Pickup Address is required")
    @Length(max = 20, message = "First-name cannot exceed 20 characters")
    private String riderFirstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 20, message = "Last-name cannot exceed 20 characters")
    private String riderLastName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 characters")
    private String phone;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be greater than or equal to 0")
    @Max(value = 120, message = "Age must be less than or equal to 120")
    private Integer age;
}
