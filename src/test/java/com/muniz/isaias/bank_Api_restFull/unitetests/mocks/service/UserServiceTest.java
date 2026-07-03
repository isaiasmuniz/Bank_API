package com.muniz.isaias.bank_Api_restFull.unitetests.mocks.service;

import com.muniz.isaias.bank_Api_restFull.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.User;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import com.muniz.isaias.bank_Api_restFull.service.UserService;
import com.muniz.isaias.bank_Api_restFull.unitetests.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    MockUser input;

    @InjectMocks
    private UserService service;

    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp(){
        input = new MockUser();;
    }

    @Test
    void findUserById() {
        User user = input.mockEntity(4);
        User persisted = user;
        UserDTO dto = input.mockDto(4);

        when(repository.findById(4L)).thenReturn(Optional.of(persisted));

        var result = service.findUserById(dto.getUserId());

        assertNotNull(result);
        assertNotNull(result.getUserId());

        assertEquals("User name 4", result.getName());
        assertEquals("User email 4", result.getEmail());
        assertEquals("Password 4", result.getPassword());
        assertEquals(4L, result.getUserId());

        verify(repository).findById(4L);
    }

    @Test
    void createUser() {
        User user = input.mockEntity(1);
        User persisted = user;
        UserDTO dto = input.mockDto(1);

        when(repository.save(any(User.class))).thenReturn(persisted);

        var result = service.createUser(dto);
        assertNotNull(result);
        assertNotNull(result.getUserId());

        assertEquals("User name 1", result.getName());
        assertEquals("User email 1", result.getEmail());
        assertEquals("Password 1", result.getPassword());
        assertEquals(1L, result.getUserId());

        verify(repository).save(any(User.class));
    }

    @Test
    void updateUser() {
        User user = input.mockEntity(3);
        User persisted = user;
        UserDTO dto = input.mockDto(3);

        when(repository.findById(3L)).thenReturn(Optional.of(persisted));
        when(repository.save(any(User.class))).thenReturn(persisted);

        var result = service.updateUser(dto);

        assertNotNull(result);
        assertNotNull(result.getUserId());

        assertEquals("User name 3", result.getName());
        assertEquals("User email 3", result.getEmail());
        assertEquals("Password 3", result.getPassword());
        assertEquals(3L, result.getUserId());

        verify(repository).findById(3L);
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldThrowNotFoundException(){

        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findUserById(1L));

        assertEquals("Not found", exception.getMessage());

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }
}