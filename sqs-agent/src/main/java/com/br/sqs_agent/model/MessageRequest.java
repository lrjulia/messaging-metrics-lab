package com.br.sqs_agent.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequest {
    private String content;
    private long timestamp;
}