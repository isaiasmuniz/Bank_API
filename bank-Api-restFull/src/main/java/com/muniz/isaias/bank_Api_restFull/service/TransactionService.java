package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.controller.TransactionController;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    PagedResourcesAssembler<TransactionDTO> assembler;

    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class.getName());

    public TransactionDTO deposit(TransactionDTO transactionDTO, Long id)  {
        logger.info("Depositing");
        Account account = getAccount(id);
        if (transactionDTO.getType().equalsIgnoreCase("deposit")
                && transactionDTO.getValue().compareTo(BigDecimal.ZERO) > 0){

            Transaction entity = parseObject(transactionDTO, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            account.setAccountBalance(account.getAccountBalance().add(entity.getValue()));
            var dto = parseObject(repository.save(entity), TransactionDTO.class);
            addHateoasLinks(dto);
            return dto;
        }else throw new BadRequestException("Wrong transaction type or Value less than zero");

    }

    public TransactionDTO withdrawal(TransactionDTO transactionDTO, Long id){
        logger.info("Withdrawing");
        Account account = getAccount(id);
        if (transactionDTO.getType().equalsIgnoreCase("withdrawal") && transactionDTO.getValue().compareTo(BigDecimal.ZERO) > 0){

            Transaction entity = parseObject(transactionDTO, Transaction.class);
            entity.setDateHour(new Date());
            entity.setOriginAccount(account);
            if (account.getAccountBalance().compareTo(entity.getValue()) < 0) throw new
                    BadRequestException("Insufficient account balance");
            account.setAccountBalance(account.getAccountBalance().subtract(entity.getValue()));
            var dto = parseObject(repository.save(entity), TransactionDTO.class);
            addHateoasLinks(dto);

            return dto;
        }else throw new BadRequestException("Wrong transaction type or Value less than zero");
    }

    public TransactionDTO bankTransfer(TransactionDTO transaction, Long id, Long targetId){
        logger.info("Transferring");
        Account originAccount = getAccount(id);
        Account targetAccount = getAccount(targetId);
        if (!targetAccount.getStatus()) throw new BadRequestException("Blocked account");
        if (transaction.getType().equalsIgnoreCase("transfer") && transaction.getValue().compareTo(BigDecimal.ZERO) > 0){


            var entity = parseObject(transaction, Transaction.class);
            entity.setDateHour(new Date());
            if(originAccount.getAccountBalance().compareTo(transaction.getValue()) >= 0){
                originAccount.setAccountBalance(originAccount.getAccountBalance().subtract(transaction.getValue()));
            }else throw new BadRequestException("Insufficient account balance");
            targetAccount.setAccountBalance(targetAccount.getAccountBalance().add(transaction.getValue()));
            entity.setTargetAccount(targetAccount);
            entity.setOriginAccount(originAccount);
            var dto = parseObject(repository.save(entity), TransactionDTO.class);
            addHateoasLinks(dto);

            return dto;
        }else throw new BadRequestException("Wrong transaction type or Value less than zero");
    }

    public PagedModel<EntityModel<TransactionDTO>> viewHistory(Long id, Pageable pageable) {
        logger.info("Viewing history");
        var result = repository.viewAllHistory(id, pageable).map(transaction -> {
            var dto = parseObject(transaction, TransactionDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(methodOn(TransactionController.class).viewHistory(id,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())
        )).withRel("viewHistory").withType("GET");

        return assembler.toModel(result, findAllLinks);
    }

    private Account getAccount(Long id) {
        var result = accountRepository.findById(id).orElseThrow(() -> new NotFoundException());
        if (!result.getStatus()) throw new BadRequestException("Blocked account");
        return result;
    }

    private void addHateoasLinks(TransactionDTO dto){
        dto.add(linkTo(methodOn(TransactionController.class).deposit(dto, dto.getOriginAccount().getAccountId())).withRel("deposit").withType("PUT"));
        dto.add(linkTo(methodOn(TransactionController.class).withdrawal(dto, dto.getOriginAccount().getAccountId())).withRel("withdrawal").withType("PUT"));
        dto.add(linkTo(methodOn(TransactionController.class).viewHistory(dto.getOriginAccount().getAccountId(), 0, 2, "asc")).withRel("viewHistory").withType("GET"));
        if (dto.getTargetAccount() != null){
            dto.add(linkTo(methodOn(TransactionController.class).bankTransfer(dto, dto.getOriginAccount().getAccountId(), dto.getTargetAccount().getAccountId())).withRel("bankTransfer").withType("PUT"));
        }
    }

}
