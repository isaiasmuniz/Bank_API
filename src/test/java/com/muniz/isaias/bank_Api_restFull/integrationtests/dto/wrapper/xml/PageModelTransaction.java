package com.muniz.isaias.bank_Api_restFull.integrationtests.dto.wrapper.xml;

import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.TransactionDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PageModelTransaction {

    @XmlElement(name = "content")
    public List<TransactionDTO> content;

    public PageModelTransaction() {}

    public List<TransactionDTO> getContent() {
        return content;
    }

    public void setContent(List<TransactionDTO> content) {
        this.content = content;
    }
}
