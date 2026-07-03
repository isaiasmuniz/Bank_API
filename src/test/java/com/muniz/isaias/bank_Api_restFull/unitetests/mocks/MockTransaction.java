package com.muniz.isaias.bank_Api_restFull.unitetests.mocks;

import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockTransaction {

    MockAccount mockAccount = new MockAccount();

    public TransactionDTO mockDto(Integer number){
        TransactionDTO dto = new TransactionDTO();
        AccountDTO targetAccount = mockAccount.mockDto(number + 2);
        AccountDTO originAccount = mockAccount.mockDto(number);
        dto.setTransactionId(number.longValue());
        dto.setValue(BigDecimal.valueOf(number));
        dto.setDateHour(new Date());
        dto.setOriginAccount(originAccount);
        dto.setTargetAccount(targetAccount);

        return dto;
    }

    public Transaction mockEntity(Integer number){
        Transaction entity = new Transaction();
        Account targetAccount = mockAccount.mockEntity(number + 2);
        Account originAccount = mockAccount.mockEntity(number);
        entity.setTransactionId(number.longValue());
        entity.setValue(BigDecimal.valueOf(number));
        entity.setDateHour(new Date());
        entity.setOriginAccount(originAccount);
        entity.setTargetAccount(targetAccount);
        return entity;
    }
}
