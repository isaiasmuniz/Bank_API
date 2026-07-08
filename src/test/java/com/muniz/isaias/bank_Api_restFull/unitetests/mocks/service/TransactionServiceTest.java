package com.muniz.isaias.bank_Api_restFull.unitetests.mocks.service;

import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.exception.BadRequestException;
import com.muniz.isaias.bank_Api_restFull.exception.NotFoundException;
import com.muniz.isaias.bank_Api_restFull.models.Account;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;
import com.muniz.isaias.bank_Api_restFull.repository.AccountRepository;
import com.muniz.isaias.bank_Api_restFull.repository.TransactionRepository;
import com.muniz.isaias.bank_Api_restFull.service.TransactionService;
import com.muniz.isaias.bank_Api_restFull.unitetests.mocks.MockAccount;
import com.muniz.isaias.bank_Api_restFull.unitetests.mocks.MockTransaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

import static com.muniz.isaias.bank_Api_restFull.mapper.ObjectMapper.parseListOfObjects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    MockTransaction input;

    @InjectMocks
    private TransactionService service;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp(){
        input = new MockTransaction();
    }

    @Test
    void deposit() {
        Transaction transaction = input.mockEntity(4);
        transaction.setType("deposit");
        Transaction persisted = transaction;
        TransactionDTO dto = input.mockDto(4);
        dto.setType("deposit");

        when(accountRepository.findById(4L)).thenReturn(Optional.of(persisted.getOriginAccount()));
        when(repository.save(any(Transaction.class))).thenReturn(persisted);

        var result = service.deposit(dto, dto.getTransactionId());

        assertNotNull(result);
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("deposit")
        && link.getHref().endsWith("bank-api/transaction/deposit/4") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("withdrawal")
        && link.getHref().endsWith("bank-api/transaction/withdrawal/4") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("bankTransfer")
        && link.getHref().endsWith("bank-api/transaction/transfer/4/6") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("viewHistory")
        && link.getHref().endsWith("bank-api/transaction/4") && link.getType().equals("GET")));

        assertEquals(4L, result.getTransactionId());
        assertEquals(BigDecimal.valueOf(4), result.getValue());
        assertEquals(BigDecimal.valueOf(8), result.getOriginAccount().getAccountBalance());

        verify(accountRepository).findById(4L);
        verify(repository).save(any(Transaction.class));
    }
    @Test
    void shouldThrowIllegalArgumentException(){
        Transaction transaction = input.mockEntity(2);
        transaction.setValue(BigDecimal.valueOf(-2));
        transaction.setType("deposit");
        TransactionDTO dto = input.mockDto(2);
        dto.setValue(BigDecimal.valueOf(-2));
        dto.setType("deposit");
        when(accountRepository.findById(2L)).thenReturn(Optional.of(transaction.getOriginAccount()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.deposit(dto, dto.getTransactionId()));
        assertEquals("Transaction type or Value must be greater than zero", exception.getMessage());

        verify(accountRepository).findById(2L);
        verify(repository, never()).save(any());
    }

    @Test
    void withdrawal() {
        Transaction transaction = input.mockEntity(5);
        transaction.setType("withdrawal");
        Transaction persisted = transaction;
        TransactionDTO dto = input.mockDto(5);
        dto.setType("withdrawal");

        when(accountRepository.findById(5L)).thenReturn(Optional.of(persisted.getOriginAccount()));
        when(repository.save(any(Transaction.class))).thenReturn(persisted);

        var result = service.withdrawal(dto, dto.getOriginAccount().getAccountId());

        assertNotNull(result);
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("deposit")
                && link.getHref().endsWith("bank-api/transaction/deposit/5") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("withdrawal")
                && link.getHref().endsWith("bank-api/transaction/withdrawal/5") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("bankTransfer")
                && link.getHref().endsWith("bank-api/transaction/transfer/5/7") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("viewHistory")
                && link.getHref().endsWith("bank-api/transaction/5") && link.getType().equals("GET")));

        assertEquals(5L, result.getTransactionId());
        assertEquals(5L, result.getOriginAccount().getAccountId());
        assertEquals(BigDecimal.valueOf(5), result.getValue());
        assertEquals(BigDecimal.valueOf(0), result.getOriginAccount().getAccountBalance());

        verify(accountRepository).findById(5L);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowInsufficientAccountBalanceError(){
        Transaction transaction = input.mockEntity(2);
        transaction.setValue(BigDecimal.valueOf(1000));
        transaction.setType("withdrawal");
        TransactionDTO dto = input.mockDto(2);
        dto.setValue(BigDecimal.valueOf(1000));
        dto.setType("withdrawal");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(transaction.getOriginAccount()));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.withdrawal(dto, dto.getOriginAccount().getAccountId()));

        assertEquals("Insufficient account balance", exception.getMessage());
        verify(accountRepository).findById(2L);
        verify(repository, never()).save(any());
    }

    @Test
    void bankTransfer() {
        Transaction transaction = input.mockEntity(10);
        transaction.setType("transfer");
        Transaction persisted = transaction;
        TransactionDTO dto = input.mockDto(10);
        dto.setType("transfer");

        when(accountRepository.findById(10L)).thenReturn(Optional.of(persisted.getOriginAccount()));
        when(accountRepository.findById(12L)).thenReturn(Optional.of(persisted.getTargetAccount()));
        when(repository.save(any(Transaction.class))).thenReturn(persisted);

        var result = service.bankTransfer(dto, dto.getOriginAccount().getAccountId(), dto.getTargetAccount().getAccountId());

        assertNotNull(result);
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("deposit")
                && link.getHref().endsWith("bank-api/transaction/deposit/10") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("withdrawal")
                && link.getHref().endsWith("bank-api/transaction/withdrawal/10") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("bankTransfer")
                && link.getHref().endsWith("bank-api/transaction/transfer/10/12") && link.getType().equals("PUT")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("viewHistory")
                && link.getHref().endsWith("bank-api/transaction/10") && link.getType().equals("GET")));

        assertEquals(10L, result.getOriginAccount().getAccountId());
        assertEquals(12L, result.getTargetAccount().getAccountId());
        assertEquals(BigDecimal.valueOf(22), result.getTargetAccount().getAccountBalance());
        assertEquals(BigDecimal.valueOf(0), result.getOriginAccount().getAccountBalance());

        verify(accountRepository).findById(10L);
        verify(accountRepository).findById(12L);
        verify(repository).save(any(Transaction.class));

    }

    @Test
    void viewHistory(){
        Transaction transaction = input.mockEntity(3);
        List<Transaction> history = new ArrayList<Transaction>();

        Transaction deposit = input.mockEntity(3);
        deposit.setType("deposit");
        deposit.setOriginAccount(transaction.getOriginAccount());
        history.add(deposit);

        Transaction withdrawal = input.mockEntity(3);
        withdrawal.setType("withdrawal");
        withdrawal.setOriginAccount(transaction.getOriginAccount());
        history.add(withdrawal);

        Transaction transfer = input.mockEntity(3);
        transfer.setType("transfer");
        transfer.setOriginAccount(transaction.getOriginAccount());
        history.add(transfer);

        when(accountRepository.findById(3L)).thenReturn(Optional.of(transaction.getOriginAccount()));
        when(repository.viewAllHistory(3L)).thenReturn(history);

        List<TransactionDTO> result = service.viewHistory(3L);

        assertEquals(3, result.size());
        assertEquals("deposit", result.get(0).getType());
        assertEquals("withdrawal", result.get(1).getType());
        assertEquals("transfer", result.get(2).getType());

        verify(accountRepository).findById(3L);
        verify(repository).viewAllHistory(3L);
    }

    @Test
    void shouldThrowNotFoundException(){

        when(accountRepository.findById(33L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.viewHistory(33L));

        assertEquals("Not found", exception.getMessage());

        verify(accountRepository).findById(33L);
        verify(repository, never()).save(any());
    }
}