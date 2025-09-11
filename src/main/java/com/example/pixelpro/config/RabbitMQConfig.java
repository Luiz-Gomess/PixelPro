package com.example.pixelpro.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String QUEUE_PROCESSING = "processing_queue";
    public static final String QUEUE_SAVING = "saving_queue";
    
    @Bean
    public Queue savingQueue() {
        return new Queue(QUEUE_SAVING, true);
    }

    @Bean
    public Queue processingQueue() {
        return new Queue(QUEUE_PROCESSING, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
