package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    AccountRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService service;

    public Account createAccount(Long id){

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException());

        Account account = new Account();
        account.setAccountNumber(String.valueOf(Math.random() * 100));
        account.setAccountBalance(BigDecimal.ZERO);
        account.setStatus(true);
        account.setUser(user);

        return repository.save(account);
    }

    public Account blockAccount(Long id){
        Account entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        entity.setStatus(false);
        return repository.save(entity);
    }

    public Account unBlockAccount(Long id){
        Account entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if (!entity.getStatus()) entity.setStatus(true);

        return repository.save(entity);
    }

    public Account findAccountById(Long id){
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        return entity;
    }
}
