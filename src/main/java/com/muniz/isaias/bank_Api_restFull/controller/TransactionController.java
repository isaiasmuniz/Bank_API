package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.controller.docs.TransactionControllerDocs;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bank-api/transaction")
@Tag(name = "transaction", description = "Endpoints for transaction")
public class TransactionController implements TransactionControllerDocs {

    @Autowired
    TransactionService service;

    @PutMapping(value = "/deposit/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TransactionDTO deposit(@RequestBody TransactionDTO transaction,
                                  @PathVariable("id") Long id){
        return service.deposit(transaction, id);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<TransactionDTO> viewHistory(@PathVariable("id") Long id){
        return service.viewHistory(id);
    }

    @PutMapping(value = "/withdrawal/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TransactionDTO withdrawal(@RequestBody TransactionDTO transaction,
                                  @PathVariable("id") Long id){
        return service.withdrawal(transaction, id);
    }

    @PutMapping(value = "/transfer/{id}/{target_id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TransactionDTO bankTransfer(@RequestBody TransactionDTO transaction,
                                    @PathVariable("id") Long id,
                                    @PathVariable("target_id") Long targetId){
        return service.bankTransfer(transaction, id, targetId);
    }
}
