package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    public Transaction deposit(Transaction transaction, Long id){
        if (transaction.getType().equalsIgnoreCase("deposit") && transaction.getValue().compareTo(BigDecimal.ZERO) > 0){
            Account entity = getAccount(id);

            transaction.setDateHour(new Date());
            transaction.setOriginAccount(entity);
            entity.setAccountBalance(entity.getAccountBalance().add(transaction.getValue()));
            return repository.save(transaction);
        }else throw new BadRequestException("Transaction type invalid");

    }

    public List<Transaction> viewHistory(Long accountId){
        getAccount(accountId);

        return repository.findByOriginAccountAccountId(accountId);
    }

    public Transaction withdrawal(Transaction transaction, Long id){
        Account entity = getAccount(id);
        if (transaction.getType().equalsIgnoreCase("withdrawal") && transaction.getValue().compareTo(entity.getAccountBalance()) <= 0){

            transaction.setDateHour(new Date());
            transaction.setTargetAccount(entity);
            entity.setAccountBalance(entity.getAccountBalance().subtract(transaction.getValue()));
            return repository.save(transaction);
        }else throw new BadRequestException("Transaction type invalid");
    }

    public Transaction bankTransfer(Transaction transaction, Long id, Long tagetId){
        Account entity = getAccount(id);
        if (transaction.getType().equalsIgnoreCase("transfer") && transaction.getValue().compareTo(entity.getAccountBalance()) <= 0){
            Account targetAccount = getAccount(tagetId);


            transaction.setDateHour(new Date());
            transaction.setTargetAccount(targetAccount);
            transaction.setOriginAccount(entity);
            targetAccount.setAccountBalance(targetAccount.getAccountBalance().add(transaction.getValue()));
            entity.setAccountBalance(entity.getAccountBalance().subtract(transaction.getValue()));
            return repository.save(transaction);
        }else throw new BadRequestException("Transaction type invalid");
    }

    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException());
    }
}
