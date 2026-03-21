package org.example.tuan3.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AfterTodayValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface AfterToday {

    String message() default "dueDate must be greater than current date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
