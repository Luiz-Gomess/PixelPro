package com.example.pixelpro.utils;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private List<FieldValidationError> errors = new ArrayList<>();

    public void addError(String field, String message) {
        errors.add(new FieldValidationError(field, message));
    }

    public List<FieldValidationError> getErrors() {
        return errors;
    }

    public static class FieldValidationError {
        private String field;
        private String message;

        public FieldValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}