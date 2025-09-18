package com.example.pixelpro.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String DIRECT_EXCHANGE = "jobs.direct";
    public static final String SAVE_ROUTING_KEY = "save";
    public static final String PROCESS_ROUTING_KEY = "process";

    public static final String QUEUE_PROCESSING = "processing_queue";
    public static final String QUEUE_SAVING = "saving_queue";

    public static final String DEAD_LETTER_EXCHANGE_NAME = DIRECT_EXCHANGE + ".dlx";
    public static final String QUEUE_PROCESSING_DLQ = QUEUE_PROCESSING + ".dlq";


    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    DirectExchange dlqDirectExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME);
    }

    @Bean
    Binding bindingSaveJob (Queue savingQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(savingQueue)
                .to(directExchange)
                .with(SAVE_ROUTING_KEY);
    }

    @Bean
    Binding bindingProcessJob (Queue processingQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(processingQueue)
                .to(directExchange)
                .with(PROCESS_ROUTING_KEY);
    }

    @Bean
    Binding deadLetterBindingProcessing () {
        return BindingBuilder
                .bind(deadLetterProcessingQueue())
                .to(dlqDirectExchange())
                .with(PROCESS_ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterProcessingQueue() {
        return new Queue(QUEUE_PROCESSING_DLQ);
    }
    @Bean
    public Queue savingQueue() {
        return new Queue(QUEUE_SAVING, true);
    }

    @Bean
    public Queue processingQueue() {
        return QueueBuilder.durable(QUEUE_PROCESSING)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", PROCESS_ROUTING_KEY)
                .build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
