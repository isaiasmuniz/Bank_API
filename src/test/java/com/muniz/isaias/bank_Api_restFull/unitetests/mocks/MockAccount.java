package com.muniz.isaias.bank_Api_restFull.unitetests.mocks;

import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;

import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseObject;

import java.math.BigDecimal;

public class MockAccount {

    public AccountDTO mockDto(Integer number){
        AccountDTO accountDTO = new AccountDTO();
        MockUser mock = new MockUser();
        User user = mock.mockEntity(number);
        accountDTO.setAccountId(number.longValue());
        accountDTO.setAccountNumber("Account number " + number);
        accountDTO.setAccountBalance(BigDecimal.valueOf(number));
        accountDTO.setStatus((number % 2) == 0);
        accountDTO.setUser(parseObject(user, UserDTO.class));

        return accountDTO;
    }

    public Account mockEntity(Integer number){
        Account entity = new Account();
        MockUser mock = new MockUser();
        User user = mock.mockEntity(number);
        entity.setAccountId(number.longValue());
        entity.setAccountNumber("Account number " + number);
        entity.setAccountBalance(BigDecimal.valueOf(number));
        entity.setStatus((number % 2) == 0);
        entity.setUser(user);
        return entity;
    }
}
