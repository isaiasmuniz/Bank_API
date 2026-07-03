package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseObject;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseListOfObjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class.getName());

    public TransactionDTO deposit(TransactionDTO transactionDTO, Long id){
        logger.info("Depositing");
        Account account = getAccount(id);
        if (transactionDTO.getType().equalsIgnoreCase("deposit")
                && transactionDTO.getValue().compareTo(BigDecimal.ZERO) > 0){

            Transaction entity = parseObject(transactionDTO, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            account.setAccountBalance(account.getAccountBalance().add(entity.getValue()));
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type or Value must be greater than zero");

    }

    public TransactionDTO withdrawal(TransactionDTO transactionDTO, Long id){
        logger.info("Withdrawing");
        Account account = getAccount(id);
        if (transactionDTO.getType().equalsIgnoreCase("withdrawal") && transactionDTO.getValue().compareTo(BigDecimal.ZERO) > 0){

            Transaction entity = parseObject(transactionDTO, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            if (account.getAccountBalance().compareTo(entity.getValue()) < 0) throw new
                    BadRequestException("Insufficient account balance");
            account.setAccountBalance(account.getAccountBalance().subtract(entity.getValue()));
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type invalid or Value must be greater than zero");
    }

    public TransactionDTO bankTransfer(TransactionDTO transaction, Long id, Long targetId){
        logger.info("Transferring");
        Account originAccount = getAccount(id);
        Account targetAccount = getAccount(targetId);
        if (transaction.getType().equalsIgnoreCase("transfer") && transaction.getValue().compareTo(BigDecimal.ZERO) > 0){


            var entity = parseObject(transaction, Transaction.class);
            entity.setDateHour(new Date());
            if(originAccount.getAccountBalance().compareTo(transaction.getValue()) >= 0){
                originAccount.setAccountBalance(originAccount.getAccountBalance().subtract(transaction.getValue()));
            }else throw new BadRequestException("Insufficient account balance");
            targetAccount.setAccountBalance(targetAccount.getAccountBalance().add(transaction.getValue()));
            entity.setTargetAccount(targetAccount);
            entity.setOriginAccount(originAccount);
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type invalid or Value must be greater than zero");
    }

    public List<TransactionDTO> viewHistory(Long accountId){
        logger.info("Viewing history");
        getAccount(accountId);

        return parseListOfObjects(repository.viewAllHistory(accountId), TransactionDTO.class);
    }

    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException());
    }

}
