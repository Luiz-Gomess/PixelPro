package com.example.pixelpro.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.model.JobListDTO;
import com.example.pixelpro.model.JobPostDTO;
import com.example.pixelpro.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/job")
public class ImageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JobRepository repository;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> process (
        @RequestPart("imagem") MultipartFile image, 
        @Valid @RequestPart("dados") JobPostDTO jobData ) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(jobData);
            Job job = mapper.readValue(json, Job.class);

            job.addOriginalImage(image);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SAVING, job);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing image: " + e.getMessage());
        }
        
        return ResponseEntity.accepted().body("Job received and being processed.");
    }

    @GetMapping
    public ResponseEntity<List<JobListDTO>> list() {
        return ResponseEntity.ok(repository.findAll().stream().map(JobListDTO::new).toList());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Object> getResult (@PathVariable Long id) {
        Job job = repository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body("Job not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + job.getImageFilename() + "_" + job.getOperationType() + ".jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(job.getImageResult());

    }
    
    
}
