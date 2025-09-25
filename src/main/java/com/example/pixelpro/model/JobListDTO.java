package com.example.pixelpro.model;

import java.time.Instant;

import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.enums.OperationType;

import lombok.Data;

@Data
public class JobListDTO {
    
    private Long id;
    private JobStatus status;
    private OperationType operationType;
    private Instant receivedAt;
    private Instant finishedAt;
    private String imageFilename;
    private String urlResultImage = "";

    public JobListDTO(Job job, String baseURL) {
        this.id = job.getId();
        this.status = job.getStatus();
        this.operationType = job.getOperationType();
        this.receivedAt = job.getReceivedAt();
        this.finishedAt = job.getFinishedAt();
        this.imageFilename = job.getImageFilename();
        this.urlResultImage += baseURL + "/" +  this.id;
    }
}