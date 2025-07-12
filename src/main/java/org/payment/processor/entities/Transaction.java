package org.payment.processor.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private String id;


    @Column(name = "from_account_number")
    private String fromAccount;

    @Column(name = "receiver_account_number")
    private String receiverAccount;

    @Column(name = "amount")
    private double amount = 0.00;

    @Column(name = "client_id")
    private String client;

    @Column(name = "status", length = 10, nullable = false)
    private String status;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate = LocalDateTime.now();


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clienteId=" + (client != null ? client : null) +
                '}';
    }
}
