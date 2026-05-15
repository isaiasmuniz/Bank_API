package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public User createUser(User user){
        user.setCreatinoDate(new Date());

        return repository.save(user);
    }

    public User updateUser(User user){
        User entity = repository.findById(user.getUserId()).orElseThrow(() -> new NotFoundException());

        entity.setCreatinoDate(new Date());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setPassword(user.getPassword());
        entity.setAccounts(user.getAccounts());

        return repository.save(entity);
    }

    public User findUserById(Long id){
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        return entity;
    }

}
