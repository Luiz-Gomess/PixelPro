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

    /**
     * Formats the processed file name by inserting the operation type before the file extension.
     */
    public String formatProcessedFileName() {
        
        // Handles filenames with multiple dots.
        String[] name = this.imageFilename.split("[.]");

        // Converts into a ArrayList to enable addition and removal of elements.
        ArrayList<String> formatted = new ArrayList<>(Arrays.asList(name));

        // Separates the filename and the suffix.
        String suffix = formatted.removeLast();
        String filenameWithoutSuffix = String.join(".", formatted);

        // Inserts the operation type right after the filename.
        filenameWithoutSuffix += "_" + this.operationType.name();
        // Concatenates the suffix back to the modified filename.
        String filenameWithSuffix = filenameWithoutSuffix + "." + suffix;
    
        return filenameWithSuffix;
    }
    
    
    public static void main(String[] args) {
        
        Job job = new Job();
        job.setImageFilename("imagem.teste3.jpg");
        job.setOperationType(OperationType.GRAYSCALE);

        System.out.println(job.formatProcessedFileName());
    }
}