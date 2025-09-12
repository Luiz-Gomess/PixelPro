package com.example.pixelpro.utils.factories.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OperationTypeValidator implements ConstraintValidator<OperationType, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
        if (value == null) {
            return false;
        }
        switch (value.toUpperCase()) {
            case "GRAYSCALE":
            case "SEPIA":
            case "INVERT":
            case "BLUR":
                return true;
            default:
                return false;
        }
    }
    
}
