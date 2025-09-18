package com.example.pixelpro.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.pixelpro.config.RabbitMQConfig;

@Component
public class DLQProcessingConsumer {

    private static final Logger log = LoggerFactory.getLogger(DLQProcessingConsumer.class);

    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESSING_DLQ)
    public void processDLQMessage(Message message) {
        String corpoMensagem = new String(message.getBody()); 
        
        log.warn("🚨 [DLQ] Mensagem recebida na Dead Letter Queue de Nota Fiscal!");
        // log.warn("Conteúdo: {}", corpoMensagem);
        
        // A Message original contém headers com informações sobre a falha
        log.warn("Headers: {}", message.getMessageProperties().getHeaders());
    }
}
