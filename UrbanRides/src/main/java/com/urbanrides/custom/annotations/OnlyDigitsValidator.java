package com.urbanrides.custom.annotations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyDigitsValidator implements ConstraintValidator<OnlyDigits, String> {
    @Override
    public void initialize(OnlyDigits constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // allow null values
        return value.matches("\\d*"); // only digits are allowed
    }
}