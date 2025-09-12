package com.example.pixelpro.workers;

import java.io.ByteArrayOutputStream;
import java.time.Instant;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.repository.JobRepository;
import com.example.pixelpro.utils.factories.StrategyFactory;
import com.example.pixelpro.workers.strategy.ImageProcessorStrategy;

@Component
public class ImageWorker {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private JobRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SAVING)
    public void saveImage(Job job) {

        System.out.println("Saving incoming Job");
        repository.save(job);

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROCESSING, job);
    }

    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING)
    public void processImage(Job job) {
        job.setStatus(JobStatus.ON_PROGRESS);
        repository.save(job);
        
        try {
            
            System.out.println("Processing image with id: " + job.getId());
            ImageProcessorStrategy strategy = StrategyFactory.getStrategy(job.getOperationType());
            ByteArrayOutputStream imageResult = strategy.process(job);
            job.setImageResult(imageResult.toByteArray());
            System.out.println( job.getImageResult().length);

            System.out.println("Processing completed");
    
            job.setStatus(JobStatus.COMPLETED);
            job.setFinishedAt(Instant.now());
            
        } catch (Exception e) {
            System.out.println("Error processing image: " + e.getMessage());
            job.setStatus(JobStatus.FAILED);
            job.setFinishedAt(Instant.now());

        } finally {

            repository.save(job);
        }
    }
}
