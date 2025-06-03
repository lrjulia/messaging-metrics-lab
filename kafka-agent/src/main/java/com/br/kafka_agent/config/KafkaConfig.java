package com.br.kafka_agent.config;

import com.br.kafka_agent.model.MessageRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageRequest> kafkaListenerContainerFactory(
            ConsumerFactory<String, MessageRequest> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, MessageRequest>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}