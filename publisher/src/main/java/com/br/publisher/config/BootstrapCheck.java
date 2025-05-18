package com.br.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapCheck implements CommandLineRunner {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Override
    public void run(String... args) {
        System.out.println("🧪 kafka.bootstrap-servers = " + bootstrapServers);
    }
}