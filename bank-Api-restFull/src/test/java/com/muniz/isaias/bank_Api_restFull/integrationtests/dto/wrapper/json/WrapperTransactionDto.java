package com.muniz.isaias.bank_Api_restFull.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperTransactionDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private TransactionEmbeddedDto embedded;

    public WrapperTransactionDto() {}

    public TransactionEmbeddedDto getEmbedded() {
        return embedded;
    }

    public void setEmbedded(TransactionEmbeddedDto embedded) {
        this.embedded = embedded;
    }
}
