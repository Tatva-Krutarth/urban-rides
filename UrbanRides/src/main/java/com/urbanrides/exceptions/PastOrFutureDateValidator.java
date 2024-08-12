package com.urbanrides.exceptions;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PastOrFutureDateValidator implements ConstraintValidator<PastOrFutureDate, String> {

    @Override
    public void initialize(PastOrFutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            System.out.println("Input is either null or empty");
            return false; // Return false for invalid input
        }

        boolean isValid = true;
        try {
            LocalDate date = LocalDate.parse(value);
            LocalDate today = LocalDate.now();
            LocalDate minDate = today.plusMonths(6); // Minimum date is 6 months from today
            LocalDate maxDate = today.plusYears(10); // Maximum date is 10 years from today

            isValid = date.isAfter(today) && date.isAfter(minDate) && date.isBefore(maxDate);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return false; // Return false for invalid date format
        }

        System.out.println("Date validation result: " + isValid);
        return isValid;
    }


}