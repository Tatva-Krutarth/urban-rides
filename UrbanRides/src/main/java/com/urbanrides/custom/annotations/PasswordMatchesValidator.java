package com.urbanrides.custom.annotations;


import com.urbanrides.dtos.UserRegistrationDto;
import org.springframework.validation.ValidationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) value;
        String password = userRegistrationDto.getPassword();
        String confPass = userRegistrationDto.getConfPass();

        if (!password.equals(confPass)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match").addConstraintViolation();
            return false;
        }
        return true;
    }
}