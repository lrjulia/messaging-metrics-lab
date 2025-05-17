package com.br.rabbitmq_publisher.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequest {
    private String content;
    private long timestamp;
}