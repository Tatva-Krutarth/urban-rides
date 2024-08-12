package com.urbanrides.custom.annotations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipartFileValidator implements ConstraintValidator<ValidMultipartFile, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(ValidMultipartFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {

            return true; // or false, depending on your requirements
        }
        System.out.println("we ar eint eh coustonm vaidaiotn ");
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("jpg") &&!fileExtension.equalsIgnoreCase("png")) {
            context.buildConstraintViolationWithTemplate("Only JPG and PNG files are allowed")
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