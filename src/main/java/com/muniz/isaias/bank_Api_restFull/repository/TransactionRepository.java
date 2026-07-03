package com.muniz.isaias.bank_Api_restFull.repository;

import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.originAccount.accountId = :id OR t.targetAccount.accountId = :id")
    List<Transaction> viewAllHistory(@Param("id") Long id);
}
