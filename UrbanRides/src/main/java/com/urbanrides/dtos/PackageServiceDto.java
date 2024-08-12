package com.urbanrides.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class PackageServiceDto {


    @NotBlank(message = "Pickup Address is required")
    @Length(max = 200, message = "Pickup address cannot be more than 200 characters")
    private String pickup;

    @Length(max = 200, message = "Dropoff address cannot be more than 200 characters")
    private String dropOff;

    @NotBlank(message = "Pickup date is required")
    private String pickUpDate;

    @NotBlank(message = "DropOff date is required")
    private String dropOffDate;


    @NotBlank(message = "Pickup time is required")
    private String pickUpTime;

    @Min(value = 1, message = "Number of passengers must be at least 1")
    @Max(value = 30, message = "Number of passengers cannot be more than 30")
    private int numberOfPassengers;


    @Pattern(regexp = "\\d+", message = "Vehicle ID should only contain digits")
    @Size(min = 1, max = 4, message = "Vehicle ID should be between 1 and 4 characters long")
    private String vehicleId;


    @Pattern(regexp = "^$|^([1-7](,([1-7](?!.*\\b\\1\\b)))*?)$", message = "Invalid pickup days format. Should be comma-separated numbers between 1 and 7, with each number appearing only once.")
    private String dailyPickUp;

    //    @Min(value = 1, message = "Charges must be at least 1")
    private int charges;

    @NotBlank(message = "Emergency contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Emergency contact should be a 10-digit number")
    private String emergencyContact;

    @Size(max = 500, message = "Special instructions should not exceed 500 characters")
    private String specialInstructions;


    //    @NotBlank(message = "Distance is required")
    @Size(max = 20, message = "Distance cannot be more than 20 characters")
    private String distance;

//    //    @NotBlank(message = "Estimated time is required")
//    @Length(max = 20, message = "Estimated time cannot be more than 20 characters")
//    private String estimatedTime;

    @Size(max = 20, message = "Service type is required")
    private String serviceType;

}
