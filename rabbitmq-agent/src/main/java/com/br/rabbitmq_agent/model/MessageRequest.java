package com.br.rabbitmq_agent.model;

import lombok.Data;

@Data
public class MessageRequest {
    private String content;
    private long timestamp;
}
