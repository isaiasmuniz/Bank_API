package com.muniz.isaias.bank_Api_restFull.controller;

import com.muniz.isaias.bank_Api_restFull.controller.docs.TransactionControllerDocs;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PagedModel<EntityModel<TransactionDTO>>> viewHistory(
            @PathVariable("id")Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "2") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction

    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "transactionId"));
        return ResponseEntity.ok(service.viewHistory(id, pageable));
    }

    @PutMapping(value = "/deposit/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TransactionDTO deposit(@RequestBody TransactionDTO transaction,
                                  @PathVariable("id") Long id){
        return service.deposit(transaction, id);
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
