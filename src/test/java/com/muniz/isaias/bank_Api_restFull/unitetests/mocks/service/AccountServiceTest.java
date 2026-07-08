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
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAccountById")
        && link.getHref().endsWith("bank-api/account/2") && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("createAccount")
        && link.getHref().endsWith("bank-api/account/2") && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("blockAccount")
        && link.getHref().endsWith("bank-api/account/block/2") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("unBlockAccount")
        && link.getHref().endsWith("bank-api/account/unblock/2") && link.getType().equals("PUT")));

        assertEquals("Account number 2", result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(2), result.getAccountBalance());
        assertTrue(result.isStatus());

        verify(repository).findById(2L);
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
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAccountById")
                && link.getHref().endsWith("bank-api/account/1") && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("createAccount")
                && link.getHref().endsWith("bank-api/account/1") && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("blockAccount")
                && link.getHref().endsWith("bank-api/account/block/1") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("unBlockAccount")
                && link.getHref().endsWith("bank-api/account/unblock/1") && link.getType().equals("PUT")));

        verify(repository).save(any(Account.class));
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
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAccountById")
                && link.getHref().endsWith("bank-api/account/6") && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("createAccount")
                && link.getHref().endsWith("bank-api/account/6") && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("blockAccount")
                && link.getHref().endsWith("bank-api/account/block/6") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("unBlockAccount")
                && link.getHref().endsWith("bank-api/account/unblock/6") && link.getType().equals("PUT")));

        assertFalse(result.isStatus());

        verify(repository).findById(6L);
        verify(repository).save(any(Account.class));
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
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAccountById")
                && link.getHref().endsWith("bank-api/account/3") && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("createAccount")
                && link.getHref().endsWith("bank-api/account/3") && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("blockAccount")
                && link.getHref().endsWith("bank-api/account/block/3") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("unBlockAccount")
                && link.getHref().endsWith("bank-api/account/unblock/3") && link.getType().equals("PUT")));

        assertTrue(result.isStatus());

        verify(repository).findById(3L);
        verify(repository).save(any(Account.class));
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