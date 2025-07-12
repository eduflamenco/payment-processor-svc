package org.payment.processor.repository.rabbitmq.contratcs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record TransactionMessage(
    String transactionId,
    String transactionSender
){
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public String toString(){
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error serializando a JSON";
        }
    }

}
