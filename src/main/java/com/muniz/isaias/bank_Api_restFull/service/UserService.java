package com.muniz.isaias.bank_Api_restFull.service;

import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseListOfObjects;
import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseObject;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    public UserDTO createUser(UserDTO user){

        logger.info("Creating User");
        var entity = parseObject(user, User.class);
        entity.setCreationDate(new Date());

        return parseObject(repository.save(entity), UserDTO.class);
    }

    public UserDTO updateUser(UserDTO userDTO){

        logger.info("Updating a User");
        var entity = repository.findById(userDTO.getUserId()).orElseThrow(() -> new NotFoundException());

        entity.setEmail(userDTO.getEmail());
        entity.setName(userDTO.getName());
        entity.setPassword(userDTO.getPassword());

        return parseObject(repository.save(entity), UserDTO.class);
    }

    public UserDTO findUserById(Long id){
        logger.info("Finding a User by id");
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException());

        return parseObject(entity, UserDTO.class);
    }
}
