package com.example.pixelpro.model;

import com.example.pixelpro.utils.factories.validators.OperationType;


public class JobPostDTO {

    
    @OperationType
    private String operationType;

    // getter e setter
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
}