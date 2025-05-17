package com.br.rabbitmq_agent.service;

import com.br.rabbitmq_agent.config.RabbitConfig;
import com.br.rabbitmq_agent.model.MessageRequest;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    private final DistributionSummary latencyHistogram;

    public MessageListener(MeterRegistry meterRegistry) {
        this.latencyHistogram = DistributionSummary.builder("rabbitmq_message_latency_ms")
            .publishPercentiles(0.5, 0.95, 0.99)
            .description("Latência das mensagens RabbitMQ em milissegundos")
            .baseUnit("milliseconds")
            .register(meterRegistry);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(MessageRequest message) {
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        System.out.println("Received: " + message.getContent() + " | Latência: " + latency + " ms");
        latencyHistogram.record(latency);
    }
}
