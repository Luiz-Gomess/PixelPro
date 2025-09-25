package com.example.pixelpro.utils.validators;

import com.example.pixelpro.enums.OperationType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OperationTypeValidator implements ConstraintValidator<OperationTypeEnumValidator, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            // If the value received is one of the enum values, it's valid.
            OperationType.valueOf(value.toUpperCase());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
