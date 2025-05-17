package com.br.publisher.controller;


import com.br.publisher.model.MessageRequest;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;

@RestController
public class KafkaController {

    private final KafkaTemplate<String, MessageRequest> kafkaTemplate;
    private final String topicName;
    private final Counter messageCounter;
    private final MessageSource messageSource;

    public KafkaController(KafkaTemplate<String, MessageRequest> kafkaTemplate,
                             @Value("${kafka.topic}") String topicName,
                             MeterRegistry registry, MessageSource messageSource) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
        this.messageCounter = registry.counter("messages.kafka.sent.total");
        this.messageSource = messageSource;
    }

    @PostMapping("/kafka/message")
    public ResponseEntity<Void> publish(@RequestBody String message) {
        MessageRequest kafkaMessage = MessageRequest.builder()
                .content(message)
                .timestamp(System.currentTimeMillis())
                .build();
        kafkaTemplate.send(topicName, kafkaMessage);
        messageCounter.increment();
        return ResponseEntity.ok().build();
    }
}

