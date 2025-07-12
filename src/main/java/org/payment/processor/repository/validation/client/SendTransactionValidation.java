package org.payment.processor.repository.validation.client;

import org.payment.processor.contracts.PaymentResponse;
import org.payment.processor.repository.validation.contracts.PaymentRequest;
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

    public PaymentResponse validateTransaction(PaymentRequest request){
        PaymentResponse result = null;
        try {
            result = restTemplate.postForObject(Objects.requireNonNull(env.getProperty("services.payment.validator")), request, PaymentResponse.class);
       }catch (Exception ex){

       }
        return result;
    }

}
