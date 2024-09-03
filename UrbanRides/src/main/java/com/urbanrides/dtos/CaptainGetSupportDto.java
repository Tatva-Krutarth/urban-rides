

package com.urbanrides.dtos;


import com.urbanrides.custom.annotations.PdfValidMultipartFile;
import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CaptainGetSupportDto {

    @Positive(message = "Support type ID must be positive")
    private int supportType;
    @NotBlank(message = "Support message cannot be blank")
    @Size(max = 150, message = "Support message must be less than 150 characters")
    private String description;
    @PdfValidMultipartFile(maxSize = 1048576, message = "Uploaded file size should not exceed 1MB adn its format should be Pdf")
    private CommonsMultipartFile uploadFile;
}

