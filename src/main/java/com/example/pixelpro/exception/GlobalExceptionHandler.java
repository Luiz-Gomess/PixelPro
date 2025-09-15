package com.example.pixelpro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.pixelpro.utils.ValidationErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(BindException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
