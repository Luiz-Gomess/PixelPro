package com.example.pixelpro.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.repository.JobRepository;




@RestController
@RequestMapping("api/v1/job")
public class ImageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JobRepository repository;
    
    @PostMapping
    public ResponseEntity<String> process (@RequestBody Job job, MultipartFile image) {

        try {
            job.setOriginalImage(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing image");
        }

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_SAVING, job);
        // repository.save(job);
        // rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROCESSING, job);

        return ResponseEntity.accepted().body("Job received and being processed.");
    }

    @GetMapping
    public ResponseEntity<List<Job>> list() {
        return ResponseEntity.ok(repository.findAll());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Object> getResult (@PathVariable Long id) {
        Job job = repository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body("Job not found");
        }

        String message = "";

        switch (job.getStatus()) {
            case JobStatus.PENDING:
                message = "Your job is still pending.";
                break;
            case JobStatus.ON_PROGRESS:
                message = "Your job is being processed!";
                break;
            case JobStatus.COMPLETED:
                message = "Your job with" + job.getOperationType() + " is completed!";
                return ResponseEntity.ok().body(job);
            default:
                break;
        }
        return ResponseEntity.ok().body(message);

    }
    
    
}
