package com.br.kafka_agent.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "agent-topic", groupId = "agent-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Mensagem Kafka recebida: " + record.value());
    }
}