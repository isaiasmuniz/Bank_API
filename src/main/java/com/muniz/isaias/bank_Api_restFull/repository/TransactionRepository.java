package com.muniz.isaias.bank_Api_restFull.repository;

import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByOriginAccountAccountId(Long accountId);
}
