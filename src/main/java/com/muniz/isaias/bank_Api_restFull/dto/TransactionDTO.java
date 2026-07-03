package com.muniz.isaias.bank_Api_restFull.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class TransactionDTO implements Serializable {
    private Long transactionId;
    private String type;
    private BigDecimal value;
    private Date dateHour;
    private AccountDTO originAccount;
    private AccountDTO targetAccount;
    private String status;

    public TransactionDTO() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getDateHour() {
        return dateHour;
    }

    public void setDateHour(Date dateHour) {
        this.dateHour = dateHour;
    }

    public AccountDTO getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(AccountDTO originAccount) {
        this.originAccount = originAccount;
    }

    public AccountDTO getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(AccountDTO targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO dto = (TransactionDTO) o;
        return Objects.equals(transactionId, dto.transactionId) && Objects.equals(type, dto.type) && Objects.equals(value, dto.value) && Objects.equals(dateHour, dto.dateHour) && Objects.equals(originAccount, dto.originAccount) && Objects.equals(targetAccount, dto.targetAccount) && Objects.equals(status, dto.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, type, value, dateHour, originAccount, targetAccount, status);
    }
}
