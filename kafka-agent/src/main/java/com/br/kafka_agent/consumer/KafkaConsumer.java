package com.br.kafka_agent.consumer;

import com.br.kafka_agent.model.MessageRequest;
import org.springframework.kafka.annotation.KafkaListener;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final DistributionSummary latencyHistogram;

    public KafkaConsumer(MeterRegistry meterRegistry) {
        this.latencyHistogram = DistributionSummary.builder("kafka_message_latency_ms")
                .publishPercentiles(0.5, 0.95, 0.99)
                .description("Latência das mensagens Kafka em milissegundos")
                .register(meterRegistry);
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "agent-group", containerFactory = "kafkaListenerContainerFactory")
    public void receive(MessageRequest message, Acknowledgment ack) {
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        System.out.println("Received: " + message.getContent() + " | Latência: " + latency + " ms");
        latencyHistogram.record(latency);
        ack.acknowledge();
    }
}