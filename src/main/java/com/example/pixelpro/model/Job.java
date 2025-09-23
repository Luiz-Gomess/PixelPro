package com.example.pixelpro.model;

import java.io.IOException;
import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

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
import jakarta.persistence.Transient;

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

    private String imageFilename;

    @Transient
    private MultipartFile image;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] originalImage;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageResult;
    

    public Job (OperationType operationType) {
        this.status = JobStatus.PENDING;
        this.operationType = operationType;
        this.receivedAt = Instant.now();
    }

    public void addOriginalImage (MultipartFile image) throws IOException {
        this.image = image;
        this.originalImage = image.getBytes();
        this.imageFilename = image.getOriginalFilename();
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
    public String getImageFilename() {
        return imageFilename;
    }
    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
    public void setOriginalImage(byte[] originalImage) {
        this.originalImage = originalImage;
    }
    public byte[] getImageResult() {
        return imageResult;
    }
    public void setImageResult(byte[] imageResult) {
        this.imageResult = imageResult;
    }
    public MultipartFile getImage () {
        return this.image;
    }
    
}