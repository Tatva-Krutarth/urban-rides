package com.urbanrides.exceptions;


import java.time.LocalDate;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PastOrFutureDateValidator implements ConstraintValidator<PastOrFutureDate, String> {

    @Override
    public void initialize(PastOrFutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate date = LocalDate.parse(value);
        LocalDate minDate = LocalDate.of(2024, 6, 1);
        LocalDate maxDate = LocalDate.of(2040, 1, 1);

        return date.isAfter(minDate) && date.isBefore(maxDate);
    }
}