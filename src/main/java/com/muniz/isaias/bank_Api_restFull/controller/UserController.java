package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.service.AccountService;
import com.muniz.isaias.bank_Api_restFull.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bank-api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public User create(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long id){
        return userService.findUserById(id);
    }

}
