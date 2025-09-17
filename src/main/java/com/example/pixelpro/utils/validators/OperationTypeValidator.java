package com.example.pixelpro.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

import com.example.pixelpro.enums.OperationType;

public class OperationTypeValidator implements ConstraintValidator<OperationTypeEnumValidator, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        OperationType enumValue;
        try {
            enumValue = OperationType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return false;
        }
 
        // If the value received is one of the enum values, it's valid.
        if (Arrays
                .asList(OperationType.values())
                .contains(enumValue)) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
