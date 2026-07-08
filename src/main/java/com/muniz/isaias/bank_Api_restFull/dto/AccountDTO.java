package com.muniz.isaias.bank_Api_restFull.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class AccountDTO extends RepresentationModel<AccountDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

   private Long accountId;
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
        AccountDTO dto = (AccountDTO) o;
        return status == dto.status && Objects.equals(accountId, dto.accountId) && Objects.equals(accountNumber, dto.accountNumber) && Objects.equals(accountBalance, dto.accountBalance) && Objects.equals(user, dto.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, accountBalance, status, user);
    }
}
