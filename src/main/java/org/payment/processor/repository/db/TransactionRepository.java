package org.payment.processor.repository.db;

import org.payment.processor.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

}
