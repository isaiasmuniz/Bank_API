package com.muniz.isaias.bank_Api_restFull.dto;

import com.muniz.isaias.bank_Api_restFull.models.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

   private Long id;
   private String accountNumber;
   private BigDecimal accountBalance;
   private boolean status;
   private UserDTO user;

    public AccountDTO() {
    }

    public AccountDTO(BigDecimal accountBalance, boolean status) {
        this.accountBalance = accountBalance;
        this.status = status;
   }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return status == that.status && Objects.equals(id, that.id) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountBalance, that.accountBalance) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, accountBalance, status, user);
    }
}
