package com.muniz.isaias.bank_Api_restFull.unitetests.mocks.service;

import com.muniz.isaias.bank_Api_restFull.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.UserRepository;
import com.muniz.isaias.bank_Api_restFull.service.AccountService;
import com.muniz.isaias.bank_Api_restFull.unitetests.mocks.MockAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    MockAccount input;

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository repository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        input = new MockAccount();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAccountById() {
        Account account = input.mockEntity(2);
        Account persisted = account;
        AccountDTO dto = input.mockDto(2);

        when(repository.findById(2L)).thenReturn(Optional.of(persisted));

        var result = service.findAccountById(dto.getAccountId());

        assertNotNull(result);
        assertNotNull(result.getAccountId());

        assertEquals("Account number 2", result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(2), result.getAccountBalance());
        assertTrue(result.isStatus());

        assertEquals("User name 2", result.getUser().getName());
        assertEquals("User email 2", result.getUser().getEmail());
        assertEquals("Password 2", result.getUser().getPassword());

    }

    @Test
    void createAccount() {
        Account account = input.mockEntity(1);
        Account persisted = account;
        AccountDTO dto = input.mockDto(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(persisted.getUser()));
        when(repository.save(any(Account.class))).thenReturn(persisted);

        var result = service.createAccount(dto.getAccountId());

        assertNotNull(result);
        assertNotNull(result.getAccountId());

        assertEquals("Account number 1", result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1), result.getAccountBalance());
        assertFalse(result.isStatus());

        assertEquals("User name 1", result.getUser().getName());
        assertEquals("User email 1", result.getUser().getEmail());
        assertEquals("Password 1", result.getUser().getPassword());
    }

    @Test
    void blockAccount() {
        Account account = input.mockEntity(6);
        Account persisted = account;
        AccountDTO dto = input.mockDto(6);

        when(repository.findById(6L)).thenReturn(Optional.of(persisted));
        when(repository.save(any(Account.class))).thenReturn(persisted);

        var result = service.blockAccount(dto.getAccountId());

        assertNotNull(result);
        assertNotNull(result.getAccountId());

        assertFalse(result.isStatus());
    }

    @Test
    void unBlockAccount() {
        Account account = input.mockEntity(3);
        Account persisted = account;
        AccountDTO dto = input.mockDto(3);

        when(repository.findById(3L)).thenReturn(Optional.of(persisted));
        when(repository.save(any(Account.class))).thenReturn(persisted);

        var result = service.unBlockAccount(dto.getAccountId());

        assertNotNull(result);
        assertNotNull(result.getAccountId());

        assertTrue(result.isStatus());
    }

    @Test
    void shouldThrowNotFoundException(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> service.blockAccount(1L));

        assertEquals("Not found", exception.getMessage());

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowAccountAlreadyBlocked(){
        Account account = input.mockEntity(3);
        Account persisted = account;
        when(repository.findById(3L)).thenReturn(Optional.of(persisted));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.blockAccount(persisted.getAccountId()));

        assertEquals("Account already blocked", exception.getMessage());

        verify(repository).findById(3L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowAccountActive(){
        Account account = input.mockEntity(2);
        Account peristed = account;

        when(repository.findById(2L)).thenReturn(Optional.of(peristed));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.unBlockAccount(peristed.getAccountId()));

        assertEquals("Active account", exception.getMessage());

        verify(repository).findById(2L);
        verify(repository, never()).save(any());
    }
}