package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.controller.docs.AccountControllerDocs;
import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bank-api/account")
@Tag(name = "accounts", description = "Endpoints for users")
public class AccountController implements AccountControllerDocs {

    @Autowired
    AccountService service;

    @PostMapping("/{id}")
    public AccountDTO createAccount(@PathVariable("id") Long id){
        return service.createAccount(id);
    }

    @GetMapping("/{id}")
    public AccountDTO findAccountById(@PathVariable("id") Long id){
        return service.findAccountById(id);
    }

    @PutMapping("/block/{id}")
    public AccountDTO blockAccount(@PathVariable("id") Long id){
        return service.blockAccount(id);
    }

    @PutMapping("/unblock/{id}")
    public AccountDTO unBlockAccount(@PathVariable("id") Long id){
        return service.unBlockAccount(id);
    }
}
