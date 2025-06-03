package com.br.publisher.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.br.publisher.model.MessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;
    private final String queueUrl;
    private final Counter messageCounter;

    public SqsController(AmazonSQS amazonSQS,
                         @Value("${app.sqs.queue-url}") String queueUrl,
                         MeterRegistry registry) {
        this.amazonSQS = amazonSQS;
        this.queueUrl = queueUrl;
        this.objectMapper = new ObjectMapper();
        this.messageCounter = registry.counter("messages.sqs.sent.total");
    }

    @PostMapping("/message")
    public ResponseEntity<Void> publishToSqs(@RequestBody MessageRequest messageRequest) throws JsonProcessingException {
        messageRequest.setTimestamp(System.currentTimeMillis());
        String messageJson = objectMapper.writeValueAsString(messageRequest);

        amazonSQS.sendMessage(new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageJson));

        messageCounter.increment();
        return ResponseEntity.ok().build();
    }
}