package org.payment.processor.process;

import org.payment.processor.configuration.RabbitConfig;
import org.payment.processor.contracts.PaymentResponse;
import org.payment.processor.contracts.PaymentTransaction;
import org.payment.processor.entities.Transaction;
import org.payment.processor.repository.db.TransactionRepository;
import org.payment.processor.repository.rabbitmq.contratcs.TransactionMessage;
import org.payment.processor.repository.validation.client.SendTransactionValidation;
import org.payment.processor.repository.validation.contracts.PaymentRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ProcessTransaction {

    private final SendTransactionValidation sendTransactionValidation;
    private final RabbitTemplate rabbitTemplate;
    private final TransactionRepository  transactionRepository;
    private final Environment env;

    public ProcessTransaction(SendTransactionValidation sendTransactionValidation, RabbitTemplate rabbitTemplate, TransactionRepository  transactionRepository, Environment env) {
        this.sendTransactionValidation = sendTransactionValidation;
        this.rabbitTemplate = rabbitTemplate;
        this.transactionRepository = transactionRepository;
        this.env = env;
    }

    public PaymentResponse executePayment(PaymentTransaction paymentTransaction){
        PaymentResponse resp = null;
        try{
            PaymentRequest validTrxReq = buildValidationRequest(paymentTransaction);
            resp = sendTransactionValidation.validateTransaction(validTrxReq);
            if (!resp.status().equals("success") || resp.paymentTotal() <= 0){
                throw new Exception("Payment Transaction Validation Failed");
            }
            String result = sendMessage(new TransactionMessage(resp.transactionId(), env.getProperty("application.properties.account-owner")));
            String finalStatus = result.equals("Success") ? "success" : "queuing failure";
            transactionRepository.save(buildTransactionRecord(resp, validTrxReq, finalStatus));
        }catch (Exception e){
            System.out.println("Validation Failed" + e.getMessage());
        }
        return resp;
    }

    private String sendMessage(TransactionMessage transaction) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE_NAME,
                    RabbitConfig.ROUTING_KEY,
                    transaction.toString()
            );
            System.out.println("Message sent");
        }catch (Exception e) {
            e.printStackTrace();
            return "Error sending message";
        }
        return "Success";
    }


    private PaymentRequest buildValidationRequest(PaymentTransaction paymentTransaction){
        PaymentRequest  request = new PaymentRequest();
        request.setCustomerId(paymentTransaction.customerId());
        request.setPaymentMethod(paymentTransaction.paymentMethod());
        request.setReceiverAccountId(env.getProperty("application.properties.account-id"));
        request.setPaymentAmount(paymentTransaction.paymentAmount());
        request.setCreditCardDetails(paymentTransaction.creditCardDetails());
        return  request;
    }

    private Transaction buildTransactionRecord(PaymentResponse resp, PaymentRequest paymentRequest, String finalStatus){
        Transaction   transaction = new Transaction();
        transaction.setId(resp.transactionId());
        transaction.setAmount(resp.paymentTotal());
        transaction.setStatus(finalStatus);
        transaction.setFromAccount(paymentRequest.getCreditCardDetails().cardNumber());
        transaction.setReceiverAccount(paymentRequest.getReceiverAccountId());
        transaction.setClient(paymentRequest.getCustomerId());
        return transaction;
    }

}
