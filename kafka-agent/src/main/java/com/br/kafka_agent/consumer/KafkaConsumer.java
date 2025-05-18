package com.br.kafka_agent.consumer;

import com.br.kafka_agent.model.MessageRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final DistributionSummary latencyHistogram;

    public KafkaConsumer(MeterRegistry meterRegistry) {
        this.latencyHistogram = DistributionSummary.builder("kafka_message_latency_ms")
                .publishPercentiles(0.5, 0.95, 0.99)
                .description("Latência das mensagens Kafka em milissegundos")
                .baseUnit("milliseconds")
                .register(meterRegistry);
    }

    @KafkaListener(topics = "${app.kafka.topic}")
    public void receive(MessageRequest message) {
//        MessageRequest message = record.value();
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        System.out.println("Received: " + message.getContent() + " | Latência: " + latency + " ms");
        latencyHistogram.record(latency);
    }
}