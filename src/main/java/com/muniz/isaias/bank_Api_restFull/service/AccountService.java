package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseObject;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseListOfObjects;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    AccountRepository repository;

    @Autowired
    UserRepository userRepository;


    private Logger logger = LoggerFactory.getLogger(AccountService.class.getName());

    public AccountDTO createAccount(Long id){
        logger.info("Creating account");

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException());

        Account account = new Account();
        account.setAccountNumber(String.valueOf(Math.random() * 100));
        account.setAccountBalance(BigDecimal.ZERO);
        account.setStatus(true);
        account.setUser(user);

        return parseObject(repository.save(account), AccountDTO.class);
    }

    public AccountDTO blockAccount(Long id){
        logger.info("Blocking Account");
        Account entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        entity.setStatus(false);
        return parseObject(repository.save(entity), AccountDTO.class);
    }

    public AccountDTO unBlockAccount(Long id){
        logger.info("Unblocking Account");
        Account entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if (!entity.getStatus()) entity.setStatus(true);

        return parseObject(repository.save(entity), AccountDTO.class);
    }

    public AccountDTO findAccountById(Long id){
        logger.info("Finding by Id");
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        return parseObject(entity, AccountDTO.class);
    }
}
