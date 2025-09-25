package com.example.pixelpro.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.enums.OperationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
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

    private String imageIdOnMini;
    

    public Job (OperationType operationType) {
        this.status = JobStatus.PENDING;
        this.operationType = operationType;
        this.receivedAt = Instant.now();
    }
    
    public Job() {
        this.status = JobStatus.PENDING;
        this.receivedAt = Instant.now();
    }

    public String formatProcessedFileName() {
        String[] name = this.imageFilename.split("[.]");

        ArrayList<String> formatted = new ArrayList<>(Arrays.asList(name));

        String suffix = formatted.getLast();
        System.out.println(suffix);

        int suffixIndex = formatted.lastIndexOf(suffix);
        System.out.println(suffixIndex);

        formatted.add(
            suffixIndex,
            operationType.name()
        );

        return String.join(".", formatted);
    }
    
}