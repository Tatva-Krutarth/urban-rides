package com.urbanrides.dtos;

import com.urbanrides.custom.annotations.PdfValidMultipartFile;
import com.urbanrides.custom.annotations.ValidMultipartFile;
import com.urbanrides.exceptions.PastOrFutureDate;
import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CaptainPersonalDetailsDto {
    @NotNull(message = "Driving license is required")
    @PdfValidMultipartFile(maxSize = 1048576, message = "Driving license file size should not exceed 1MB")
    public CommonsMultipartFile drivingLicense;

    @NotNull(message = "Profile photo is required")
    @ValidMultipartFile(maxSize = 1048576, message = "Profile photo file size should not exceed 1MB")
    public CommonsMultipartFile profilePhoto;

    @NotNull(message = "Aadhaar card is required")
    @PdfValidMultipartFile(maxSize = 1048576, message = "Aadhaar card file size should not exceed 1MB")
    public CommonsMultipartFile adharCard;

    @NotNull(message = "Registration certificate is required")
    @PdfValidMultipartFile(maxSize = 1048576, message = "Registration certificate file size should not exceed 1MB")
    public CommonsMultipartFile registrationCertificate;

    @NotNull(message = "Expiration date is required")
    @Pattern(regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid date format")
    @PastOrFutureDate(message = "Date must be between 2024-06-01 and 2040-01-01")
    private String rcExpiration;

    @NotNull(message = "License Expiration date is required")
    @Pattern(regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid date format")
    @PastOrFutureDate(message = "Date must be between 2024-06-01 and 2040-01-01")
    private String licenseExpiration;
}