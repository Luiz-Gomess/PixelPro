package com.example.pixelpro.model;

import java.time.Instant;

import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.enums.OperationType;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private Instant receivedAt;
    private Instant finishedAt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] originalImage;

    private String imageResult;
    

    public Job ( OperationType operationType) {
        this.status = JobStatus.PENDING;
        this.operationType = operationType;
        this.receivedAt = Instant.now();
        // this.originalImage = originalImage;
    }

    

    public Job() {
        this.status = JobStatus.PENDING;
        this.receivedAt = Instant.now();
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public JobStatus getStatus() {
        return status;
    }
    public void setStatus(JobStatus status) {
        this.status = status;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
    public Instant getReceivedAt() {
        return receivedAt;
    }
    public Instant getFinishedAt() {
        return finishedAt;
    }
    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }
    public byte[] getOriginalImage() {
        return originalImage;
    }
    public void setOriginalImage(byte[] originalImage) {
        this.originalImage = originalImage;
    }
    public String getImageResult() {
        return imageResult;
    }
    public void setImageResult(String imageResult) {
        this.imageResult = imageResult;
    }
    
}