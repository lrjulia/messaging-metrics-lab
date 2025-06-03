package com.br.sqs_agent.consumer;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.br.sqs_agent.model.MessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SqsConsumer {

    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;
    private final DistributionSummary latencyHistogram;
    private final Counter deliveredCounter;

    @Value("${SQS_URL}")
    private String queueUrl;

    public SqsConsumer(AmazonSQS sqsClient, ObjectMapper objectMapper, MeterRegistry meterRegistry) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.latencyHistogram = DistributionSummary.builder("sqs_message_latency_ms")
                .publishPercentiles(0.5, 0.95, 0.99)
                .description("Latência das mensagens SQS em milissegundos")
                .register(meterRegistry);
        this.deliveredCounter = meterRegistry.counter("messages.sqs.delivered.total");
    }

    @PostConstruct
    public void startPolling() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executor.scheduleAtFixedRate(this::poll, 0, 200, TimeUnit.MILLISECONDS);
        }
    }

    private void poll() {
        ReceiveMessageRequest request = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(10);

        List<Message> messages = sqsClient.receiveMessage(request).getMessages();

        for (Message msg : messages) {
            try {
                MessageRequest message = objectMapper.readValue(msg.getBody(), MessageRequest.class);
                long now = System.currentTimeMillis();
                long latency = now - message.getTimestamp();
                latencyHistogram.record(latency);
                deliveredCounter.increment();
                sqsClient.deleteMessage(queueUrl, msg.getReceiptHandle());
                System.out.println("[SQS] Received message: " + msg.getBody());
            } catch (Exception e) {
                System.err.println("Failed to process message: " + e.getMessage());
            }
        }
    }
}