package org.payment.processor.repository;

import org.payment.processor.contracts.TransactionRequest;
import org.payment.processor.contracts.ValidationResult;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class SendTransactionValidation {

    private Environment env;
    private RestTemplate restTemplate;

    public SendTransactionValidation(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }

    public ValidationResult validateTransaction(TransactionRequest request){
        ValidationResult result = null;
        try {
            result = restTemplate.postForObject(Objects.requireNonNull(env.getProperty("services.payment.validator")), request, ValidationResult.class);
       }catch (Exception ex){

       }
        return result;
    }

}
