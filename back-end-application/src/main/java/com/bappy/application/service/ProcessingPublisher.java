package com.bappy.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessingPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    public void publishProcessingJob(String documentId, String s3path) {
        String payload = String.format("{\"documentId\":\"%s\",\"s3Path\":\"%s\"}", documentId, s3path);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, payload);
    }
}
