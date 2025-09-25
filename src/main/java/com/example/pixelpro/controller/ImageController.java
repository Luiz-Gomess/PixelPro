package com.example.pixelpro.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.model.JobListDTO;
import com.example.pixelpro.model.JobPostDTO;
import com.example.pixelpro.repository.JobRepository;
import com.example.pixelpro.services.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/job")
public class ImageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JobService jobService;

    @Autowired
    JobRepository repository;


    /**
     * Receives an image and job data, uploads the image to MinIO, maps the data to a Job entity, and sends it to RabbitMQ for processing.
     * @param image
     * @param jobData
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> process (
        @RequestPart("imagem") MultipartFile image, 
        @Valid @RequestPart("dados") JobPostDTO jobData ) {

        try {
            UUID imageIdOnMini = UUID.randomUUID();

            jobService.uploadObject(
                imageIdOnMini.toString(), 
                image.getOriginalFilename(), 
                image.getInputStream(), 
                image.getSize(), 
                image.getContentType(),
                true
            );

            Job job = jobService.map(jobData, imageIdOnMini.toString(), image);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE,RabbitMQConfig.SAVE_ROUTING_KEY, job);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing image: " + e.getMessage());
        }
        
        return ResponseEntity.accepted().body("Job received and being processed.");
    }

    /**
     * Lists jobs with pagination.
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<JobListDTO>> list(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize) {
        
        return ResponseEntity.ok(jobService.getJobs(pageNo, pageSize));
    }
    

    /**
     * Fetches the processed image for a given job ID.
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getResult (@PathVariable Long id) throws IOException {
        Job job = repository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body("Job not found");
        }

        byte[] content = jobService.getObject(job.getImageIdOnMini(), job.getImageFilename(), false).readAllBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + job.formatProcessedFileName())
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(content.length)
                .body(content);

    }
    
    
}
