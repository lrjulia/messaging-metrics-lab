package com.br.rabbitmq_publisher.controller;

import com.br.rabbitmq_publisher.model.MessageRequest;
import io.micrometer.core.instrument.Counter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class RabbitMQController {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    private final Counter messageCounter;

    public RabbitMQController(RabbitTemplate rabbitTemplate,
                              @Value("${app.queue.name}") String queueName,
                              MeterRegistry registry) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
        this.messageCounter = registry.counter("messages.sent.total");
    }

    @PostMapping("/rabbitmq/message")
    public ResponseEntity<Void> publish(@RequestBody String message) {
        MessageRequest rabbitmqMessage = MessageRequest.builder()
                .content(message)
                .timestamp(System.currentTimeMillis())
                .build();
        rabbitTemplate.convertAndSend(queueName, rabbitmqMessage);
        messageCounter.increment();
        return ResponseEntity.ok().build();
    }


}