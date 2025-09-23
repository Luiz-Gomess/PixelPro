package com.example.pixelpro.consumers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;
import com.example.pixelpro.enums.JobStatus;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.repository.JobRepository;
import com.example.pixelpro.services.JobService;
import com.example.pixelpro.utils.factories.StrategyFactory;

import jakarta.transaction.Transactional;

@Component
public class ImageWorker {

    private static final Logger log = LoggerFactory.getLogger(ImageWorker.class);


    @Autowired
    private JobService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private JobRepository repository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SAVING)
    public void saveImage(Job job) {

        try {
            service.uploadObject(
                job.getId(),
                job.getImage().getOriginalFilename(),
                job.getImage().getInputStream(),
                job.getImage().getSize(),
                job.getImage().getContentType(),
                true
            );
        } catch (IOException e) {
            log.error("Error while uploading to MiniO", e);
        }

        repository.save(job);

        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.PROCESS_ROUTING_KEY, job);
    }

    
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING)
    public void processImage(Job job) {

        job.setStatus(JobStatus.ON_PROGRESS);
        repository.save(job);
        
        try {

            log.info("Processing image with id: " + job.getId());
            ImageProcessorStrategy strategy = StrategyFactory.getStrategy(job.getOperationType());
            
            ByteArrayOutputStream imageResult = strategy.process((service.getObject(job.getId(), job.getImageFilename())).readAllBytes());
            InputStream is = new ByteArrayInputStream(imageResult.toByteArray());
            

            service.uploadObject(
                job.getId(),
                job.getImage().getOriginalFilename(),
                is,
                imageResult.size(),
                job.getImage().getContentType(),
                false
            );

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
