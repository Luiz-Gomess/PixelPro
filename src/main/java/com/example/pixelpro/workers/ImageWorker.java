package com.example.pixelpro.workers;

import java.time.Instant;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.repository.JobRepository;
import com.example.pixelpro.services.ImageServices;

@Component
public class ImageWorker {

    @Autowired
    RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ImageServices imageServices;
    
    @Autowired
    private JobRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SAVING)
    public void saveImage(Job job) {
        // TODO: Replace print with Log
        System.out.println("Saving incoming Job");
        repository.save(job);
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROCESSING, job);
    }

    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING)
    public void processImage(Job job) {
        job.setStatus(JobStatus.ON_PROGRESS);
        repository.save(job);
        
        System.out.println("Processing image with id: " + job.getId());
        imageServices.grayscale("/home/luiz/springboot/proc_images/pixelpro/src/main/resources/images/maca.jpg");
        

        System.out.println("Processing completed");
        job.setStatus(JobStatus.COMPLETED);
        job.setFinishedAt(Instant.now());
        job.setImageResult(job.getOriginalImage() + "_processed");
        repository.save(job);
    }
}
