package com.urbanrides.dtos;

import com.urbanrides.custom.annotations.ValidMultipartFile;
import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class RiderUMUpdateProfileLogoDto {

    @NotNull(message = "Profile photo is required")
    @ValidMultipartFile(maxSize = 1048576, message = "Profile photo file size should not exceed 1MB")
    public CommonsMultipartFile profilePhoto;
}
