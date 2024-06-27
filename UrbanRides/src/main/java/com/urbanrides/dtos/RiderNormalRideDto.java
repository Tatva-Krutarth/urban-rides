package com.urbanrides.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class RiderNormalRideDto {
    @NotBlank(message = "Pickup Address is required")
    @Length(max = 200, message = "Pickup address cannot be more than 200 characters")
    private String pickup;

    @NotBlank(message = "Dropoff Address is required")
    @Length(max = 200, message = "Dropoff address cannot be more than 200 characters")
    private String dropoff;


    @Pattern(regexp = "\\d+", message = "Vehicle ID should only contain digits")
    @Size(min = 1, max = 4, message = "Vehicle ID should be between 1 and 4 characters long")
    private String vehicleId;

    @NotBlank(message = "Distance is required")
    @Size(max = 20, message = "Distance cannot be more than 20 characters")
    private String distance;

    @NotBlank(message = "Estimated time is required")
    @Length(max = 20, message = "Estimated time cannot be more than 20 characters")
    private String estimatedTime;

    @Min(value = 1, message = "Charges must be at least 1")
    private int charges;
}
