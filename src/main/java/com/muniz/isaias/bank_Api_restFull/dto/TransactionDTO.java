package com.muniz.isaias.bank_Api_restFull.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class TransactionDTO implements Serializable {
    private Long id;
    private String type;
    private BigDecimal value;
    private Date dateHour;
    private AccountDTO originAccount;
    private AccountDTO targetAccount;
    private String status;

    public TransactionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        TransactionDTO that = (TransactionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(value, that.value) && Objects.equals(dateHour, that.dateHour) && Objects.equals(originAccount, that.originAccount) && Objects.equals(targetAccount, that.targetAccount) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value, dateHour, originAccount, targetAccount, status);
    }
}
