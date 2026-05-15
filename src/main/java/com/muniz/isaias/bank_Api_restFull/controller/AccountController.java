package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bank-api/account")
public class AccountController {

    @Autowired
    AccountService service;

    @PostMapping("/{id}")
    public Account createAccount(@PathVariable("id") Long id){
        return service.createAccount(id);
    }

    @GetMapping("/{id}")
    public Account findAccountById(@PathVariable("id") Long id){
        return service.findAccountById(id);
    }

    @PutMapping("/block/{id}")
    public Account blockAccount(@PathVariable("id") Long id){
        return service.blockAccount(id);
    }

    @PutMapping("/unblock/{id}")
    public Account unBlockAccount(@PathVariable("id") Long id){
        return service.unBlockAccount(id);
    }
}
