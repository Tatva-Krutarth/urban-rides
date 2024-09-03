package com.urbanrides.custom.annotations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PdfMultipartFileValidator implements ConstraintValidator<PdfValidMultipartFile, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(PdfValidMultipartFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            System.out.println("The file in the cdfsdfs is nu;;");
            return true;
        }
        System.out.println("wthis is snot emptu");
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("pdf")) {
            context.buildConstraintViolationWithTemplate("Only PDF files are allowed")
                    .addConstraintViolation();
            return false;
        }
        if (file.getSize() > maxSize) {
            context.buildConstraintViolationWithTemplate("File size must not exceed " + maxSize / 1024 + "KB")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}