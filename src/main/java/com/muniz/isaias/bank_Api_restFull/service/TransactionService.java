package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
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
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class.getName());

    public TransactionDTO deposit(TransactionDTO transaction, Long id){
        logger.info("Depositing");
        if (transaction.getType().equalsIgnoreCase("deposit") && transaction.getValue().compareTo(BigDecimal.ZERO) > 0){
            Account account = getAccount(id);

            Transaction entity = parseObject(transaction, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            account.setAccountBalance(account.getAccountBalance().add(entity.getValue()));
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type invalid");

    }

    public List<TransactionDTO> viewHistory(Long accountId){
        logger.info("Viewing history");
        getAccount(accountId);

        return parseListOfObjects(repository.findByOriginAccountAccountId(accountId), TransactionDTO.class);
    }

    public TransactionDTO withdrawal(TransactionDTO transactionDTO, Long id){
        logger.info("Withdrawing");
        Account account = getAccount(id);
        if (transactionDTO.getType().equalsIgnoreCase("withdrawal") && transactionDTO.getValue().compareTo(account.getAccountBalance()) <= 0){

            Transaction entity = parseObject(transactionDTO, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            account.setAccountBalance(account.getAccountBalance().subtract(entity.getValue()));
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type invalid");
    }

    public TransactionDTO bankTransfer(TransactionDTO transaction, Long id, Long tagetId){
        logger.info("Transferring");
        Account account = getAccount(id);
        if (transaction.getType().equalsIgnoreCase("transfer") && transaction.getValue().compareTo(account.getAccountBalance()) <= 0){
            Account targetAccount = getAccount(tagetId);


            var entity = parseObject(transaction, Transaction.class);
            entity.setDateHour(new Date());
            entity.setTargetAccount(targetAccount);
            entity.setOriginAccount(account);
            targetAccount.setAccountBalance(targetAccount.getAccountBalance().add(transaction.getValue()));
            account.setAccountBalance(account.getAccountBalance().subtract(transaction.getValue()));
            return parseObject(repository.save(entity), TransactionDTO.class);
        }else throw new BadRequestException("Transaction type invalid");
    }

    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException());
    }
}
