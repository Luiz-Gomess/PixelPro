package com.example.pixelpro.model;

import java.time.Instant;

import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.enums.OperationType;

public record JobListDTO(
    Long id,
    JobStatus status,
    OperationType operationType,
    Instant receivedAt,
    Instant finishedAt,
    String imageFilename
) {
    
    public JobListDTO (Job job) {
        this(
            job.getId(),
            job.getStatus(),
            job.getOperationType(),
            job.getReceivedAt(),
            job.getFinishedAt(),
            job.getImageFilename()
        );
    }

}
