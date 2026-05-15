package com.muniz.isaias.bank_Api_restFull.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column
    private BigDecimal accountBalance;

    @Column
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Account(BigDecimal accountBalance, boolean status) {
        this.accountBalance = accountBalance;
        this.status = status;
    }

    public Account() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return status == account.status && Objects.equals(accountId, account.accountId) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(accountBalance, account.accountBalance) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, accountBalance, status, user);
    }
}
