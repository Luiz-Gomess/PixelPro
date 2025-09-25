package com.example.pixelpro.consumers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
        repository.save(job);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.PROCESS_ROUTING_KEY, job);
    }

    /**
     * Processes the image based on the job's operation type.
     * @param job
     */
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING)
    public void processImage(Job job) {
        try {
            job.setStatus(JobStatus.ON_PROGRESS);
            repository.save(job);

            log.info("Processing job with id: " + job.getId());
            // Process the image based on the operation type using Strategy pattern.
            ImageProcessorStrategy strategy = StrategyFactory.getStrategy(job.getOperationType());
            
            ByteArrayOutputStream imageResult = strategy.process(
                (service.getObject(
                    job.getImageIdOnMini(), 
                    job.getImageFilename(), 
                    true)
                    ).readAllBytes()
                );
            InputStream is = new ByteArrayInputStream(imageResult.toByteArray());
            
            // Uploads the processed image back to MinIO.
            service.uploadObject(
                job.getImageIdOnMini(),
                job.getImageFilename(),
                is,
                imageResult.size(),
                MediaType.IMAGE_JPEG_VALUE,
                false
            );

            log.info("Processing completed");
    
            job.setStatus(JobStatus.COMPLETED);
            job.setFinishedAt(Instant.now());
            
        } catch (Exception e) {
            log.error("Error processing image: " + e.getMessage());
            job.setStatus(JobStatus.FAILED);
            job.setFinishedAt(Instant.now());

        } finally {
            repository.save(job);
        }
    }
}
