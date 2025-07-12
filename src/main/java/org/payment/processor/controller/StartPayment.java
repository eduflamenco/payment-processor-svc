package org.payment.processor.controller;

import org.payment.processor.contracts.PaymentResponse;
import org.payment.processor.contracts.PaymentTransaction;
import org.payment.processor.process.ProcessTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class StartPayment {

    private ProcessTransaction processTransaction;

    public StartPayment(ProcessTransaction processTransaction) {
        this.processTransaction = processTransaction;
    }

    @PostMapping("/start")
    public ResponseEntity<PaymentResponse> validatePayment(@RequestBody PaymentTransaction payReq){
        var resp = processTransaction.executePayment(payReq);
        return ResponseEntity.ok(resp);
    }

}
