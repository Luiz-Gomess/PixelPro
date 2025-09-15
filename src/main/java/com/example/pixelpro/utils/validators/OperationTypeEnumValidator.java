package com.example.pixelpro.utils.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint( validatedBy = OperationTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationTypeEnumValidator {
    String message() default "Invalid operation type. Operations allowed: [GRAYSCALE, SEPIA, INVERT, BLUR]";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
