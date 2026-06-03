package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bank-api/transaction")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PutMapping("/deposit/{id}")
    public TransactionDTO deposit(@RequestBody TransactionDTO transaction,
                                  @PathVariable("id") Long id){
        return service.deposit(transaction, id);
    }

    @GetMapping("/{id}")
    public List<TransactionDTO> viewHistory(@PathVariable("id") Long id){
        return service.viewHistory(id);
    }

    @PutMapping("/withdrawal/{id}")
    public TransactionDTO withdrawal(@RequestBody TransactionDTO transaction,
                                  @PathVariable("id") Long id){
        return service.withdrawal(transaction, id);
    }

    @PutMapping("/transfer/{id}/{target_id}")
    public TransactionDTO bankTransfer(@RequestBody TransactionDTO transaction,
                                    @PathVariable("id") Long id,
                                    @PathVariable("target_id") Long targetId){
        return service.bankTransfer(transaction, id, targetId);
    }
}
