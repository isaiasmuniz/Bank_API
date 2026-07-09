package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.controller.docs.UserControllerDocs;
import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.service.AccountService;
import com.muniz.isaias.bank_Api_restFull.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("bank-api/user")
@Tag(name = "users", description = "Endpoints for users")
public class UserController implements UserControllerDocs {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDTO create(@RequestBody UserDTO user){
        return userService.createUser(user);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDTO update(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDTO findUserById(@PathVariable("id") Long id){
        return userService.findUserById(id);
    }

}
