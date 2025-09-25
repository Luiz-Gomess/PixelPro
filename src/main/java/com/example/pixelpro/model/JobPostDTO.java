package com.example.pixelpro.model;

import com.example.pixelpro.utils.validators.OperationTypeEnumValidator;


public class JobPostDTO {

    // Uses custom validator to ensure only valid operation types are used.
    @OperationTypeEnumValidator
    private String operationType;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
}