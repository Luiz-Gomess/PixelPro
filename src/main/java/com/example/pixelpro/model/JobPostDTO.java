package com.example.pixelpro.model;

import com.example.pixelpro.utils.validators.OperationTypeEnumValidator;


public class JobPostDTO {

    
    @OperationTypeEnumValidator
    private String operationType;

    // getter e setter
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
}