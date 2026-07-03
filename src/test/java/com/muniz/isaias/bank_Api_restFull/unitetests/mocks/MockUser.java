package com.muniz.isaias.bank_Api_restFull.unitetests.mocks;

import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockUser {

    public UserDTO mockDto(Integer number){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(number.longValue());
        userDTO.setName("User name " + number);
        userDTO.setEmail("User email " + number);
        userDTO.setPassword("Password " + number);
        userDTO.setCreationDate(new Date());
        return userDTO;
    }

    public User mockEntity(Integer number){
        User entity = new User();
        entity.setUserId(number.longValue());
        entity.setName("User name " + number);
        entity.setEmail("User email " + number);
        entity.setPassword("Password " + number);
        entity.setCreationDate(new Date());

        return entity;
    }

}
