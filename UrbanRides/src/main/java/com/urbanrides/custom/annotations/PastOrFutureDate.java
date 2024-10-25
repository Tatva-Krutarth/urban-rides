package com.urbanrides.custom.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;




@Constraint(validatedBy = PastOrFutureDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PastOrFutureDate {
    String message() default "Invalid date range";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
