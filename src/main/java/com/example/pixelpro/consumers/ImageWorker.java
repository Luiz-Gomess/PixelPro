package com.example.pixelpro.consumers;

import java.util.Random;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pixelpro.config.RabbitMQConfig;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.repository.JobRepository;

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

        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.PROCESS_ROUTING_KEY, job);
    }

    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING)
    public void processImage(Job job) {

        Random random = new Random();
        if (random.nextInt(0,3) == 1){
            throw new RuntimeException("Dados do pedido inválidos para geração de NF.");
        }

        // job.setStatus(JobStatus.ON_PROGRESS);
        // repository.save(job);
        
        // try {

            
        //     System.out.println("Processing image with id: " + job.getId());
        //     ImageProcessorStrategy strategy = StrategyFactory.getStrategy(job.getOperationType());
        //     ByteArrayOutputStream imageResult = strategy.process(job);
        //     job.setImageResult(imageResult.toByteArray());
        //     System.out.println( job.getImageResult().length);

        //     System.out.println("Processing completed");
    
        //     job.setStatus(JobStatus.COMPLETED);
        //     job.setFinishedAt(Instant.now());
            
        // } catch (Exception e) {
        //     System.out.println("Error processing image: " + e.getMessage());
        //     job.setStatus(JobStatus.FAILED);
        //     job.setFinishedAt(Instant.now());

        // } finally {

        //     repository.save(job);
        // }
    }
}
