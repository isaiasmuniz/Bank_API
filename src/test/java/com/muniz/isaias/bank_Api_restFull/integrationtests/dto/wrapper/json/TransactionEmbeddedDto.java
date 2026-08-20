package com.muniz.isaias.bank_Api_restFull.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.TransactionDTO;

import java.io.Serializable;
import java.util.List;

public class TransactionEmbeddedDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransactionEmbeddedDto() {}

    @JsonProperty("transactionDTOList")
    private List<TransactionDTO> transactionDTOList;

    public List<TransactionDTO> getTransactionDTOList() {
        return transactionDTOList;
    }

    public void setTransactionDTOList(List<TransactionDTO> transactionDTOList) {
        this.transactionDTOList = transactionDTOList;
    }
}
