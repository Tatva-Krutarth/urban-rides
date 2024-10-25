package com.urbanrides.dtos;

import com.urbanrides.custom.annotations.PdfValidMultipartFile;
import com.urbanrides.custom.annotations.PastOrFutureDate;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.*;

@Data
public class CaptainReuploadDataDto {
    @Nullable
    @PdfValidMultipartFile(maxSize = 1048576, message = "Driving license file size should not exceed 1MB")
    private CommonsMultipartFile drivingLicensee;
    @Nullable
    @PdfValidMultipartFile(maxSize = 1048576, message = "Aadhaar card file size should not exceed 1MB")
    private CommonsMultipartFile adharCarde;
    @Nullable
    @PdfValidMultipartFile(maxSize = 1048576, message = "Registration certificate file size should not exceed 1MB")
    private CommonsMultipartFile registrationCertificatee;

    @Pattern(regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid datee format")
    @PastOrFutureDate(message = "Date must be between 2024-06-01 and 2040-01-01")
    private String rcExpiratione;

    @Pattern(regexp = "^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid datee format")
    @PastOrFutureDate(message = "Date must be between 2024-06-01 and 2040-01-01")
    private String licenseExpiratione;

    @Pattern(regexp = "^[A-Z]{2}[ -]?[0-9]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$", message = "Invalid vehicle number format")
    private String numberPlatee;
}
