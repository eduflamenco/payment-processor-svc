package org.payment.processor.repository.validation.contracts;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.payment.processor.contracts.CreditCardDetails;
import org.payment.processor.contracts.PaymentMethod;

@Data
@JsonSerialize
public class PaymentRequest {
    private String customerId;
    private Double paymentAmount;
    private String receiverAccountId;
    private PaymentMethod paymentMethod;
    private CreditCardDetails creditCardDetails;
}
