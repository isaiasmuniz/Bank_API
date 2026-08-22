package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muniz.isaias.bank_Api_restFull.integrationtests.AbstractIntegration;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.UserDTO;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.wrapper.json.WrapperTransactionDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionControllerWithXmlTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;
    private static RequestSpecification specification;
    private static UserDTO userDTO;
    private static AccountDTO accountDTO;
    private static TransactionDTO transactionDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        accountDTO = new AccountDTO();
        userDTO = new UserDTO();
        transactionDTO = new TransactionDTO();
    }

    @Test
    @Order(1)
    void deposit() throws JsonProcessingException {


        mockUser();
        TransactionDTO transactionDTO1 = new TransactionDTO("deposit", BigDecimal.valueOf(10000));
        specification = new RequestSpecBuilder().addHeader("origin", "http://localhost:8888")
                .setBasePath("bank-api/user")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var createdUser = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(userDTO)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO persistedUser = objectMapper.readValue(createdUser, UserDTO.class);
        var targetUser = userDTO;
        userDTO = persistedUser;

        var createdTargetUser = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(targetUser)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO persistedTargetUser = objectMapper.readValue(createdTargetUser, UserDTO.class);

        specification.basePath("bank-api/account");

        var createdAccount = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", persistedUser.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO persistedAccount = objectMapper.readValue(createdAccount, AccountDTO.class);
        persistedAccount.setUser(persistedUser);
        transactionDTO.setOriginAccount(persistedAccount);

        var createdTargetAccount = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", persistedTargetUser.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO persistedTargetAccount = objectMapper.readValue(createdTargetAccount, AccountDTO.class);
        persistedTargetAccount.setUser(persistedTargetUser);
        transactionDTO.setTargetAccount(persistedTargetAccount);

        assertNotNull(persistedUser.getUserId());
        assertNotNull(persistedTargetUser.getUserId());
        assertNotNull(persistedAccount.getAccountId());
        assertNotNull(persistedTargetAccount.getAccountId());

        assertEquals("Gaara", persistedTargetAccount.getUser().getName());
        assertEquals("Gaara@konaha.com", persistedTargetAccount.getUser().getEmail());
        assertEquals("AldeiaDaAreia", persistedTargetAccount.getUser().getPassword());

        specification.basePath("bank-api/transaction/deposit");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(transactionDTO1)
                .pathParams("id", persistedAccount.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .extract()
                .body().asString();

        transactionDTO1 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO1.getTransactionId());
        assertNotNull(transactionDTO1.getOriginAccount());
        assertNotNull(transactionDTO1.getOriginAccount().getUser());

        assertEquals(0, transactionDTO1.getValue().compareTo(BigDecimal.valueOf(10000)));
        assertEquals(0, transactionDTO1.getOriginAccount().getAccountBalance().compareTo(BigDecimal.valueOf(10000)));
        assertEquals("deposit", transactionDTO1.getType());
    }

    @Test
    @Order(2)
    void withdrawal() throws JsonProcessingException {

        TransactionDTO transactionDTO2 = new TransactionDTO("withdrawal", BigDecimal.valueOf(5000));
        specification.basePath("bank-api/transaction/withdrawal");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(transactionDTO2)
                .pathParam("id", transactionDTO.getOriginAccount().getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        transactionDTO2 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO2.getTransactionId());
        assertNotNull(transactionDTO2.getOriginAccount());
        assertNotNull(transactionDTO2.getOriginAccount().getUser());

        assertEquals(0, transactionDTO2.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals(0, transactionDTO2.getOriginAccount().getAccountBalance().compareTo(BigDecimal.valueOf(5000)));
        assertEquals("withdrawal", transactionDTO2.getType());
    }

    @Test
    @Order(3)
    void bankTransfer() throws JsonProcessingException {

        TransactionDTO transactionDTO3 = new TransactionDTO("transfer", BigDecimal.valueOf(5000));
        specification.basePath("bank-api/transaction/transfer");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(transactionDTO3)
                .pathParams("id", transactionDTO.getOriginAccount().getAccountId(), "targetId", transactionDTO.getTargetAccount().getAccountId())
                .when()
                .put("{id}/{targetId}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        transactionDTO3 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO3.getTransactionId());
        assertNotNull(transactionDTO3.getOriginAccount());
        assertNotNull(transactionDTO3.getTargetAccount());
        assertNotNull(transactionDTO3.getOriginAccount().getUser());
        assertNotNull(transactionDTO3.getTargetAccount().getUser());

        assertEquals(0, transactionDTO3.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals(0, transactionDTO3.getOriginAccount().getAccountBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, transactionDTO3.getTargetAccount().getAccountBalance().compareTo(BigDecimal.valueOf(5000)));
        assertEquals("transfer", transactionDTO3.getType());
    }

    @Test
    @Order(4)
    void viewHistory() throws JsonProcessingException {

        specification.basePath("bank-api/transaction");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", transactionDTO.getOriginAccount().getAccountId())
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        WrapperTransactionDto wrapper = objectMapper.readValue(content, WrapperTransactionDto.class);
        List<TransactionDTO> transactionDTOList = wrapper.getEmbedded().getTransactionDTOList();

        assertEquals(3, transactionDTOList.size());

        TransactionDTO transaction1 = transactionDTOList.get(0);

        assertNotNull(transaction1.getTransactionId());
        assertNotNull(transaction1.getOriginAccount());
        assertNotNull(transaction1.getOriginAccount().getUser());

        assertTrue(transaction1.getOriginAccount().isStatus());
        assertEquals(0, transaction1.getValue().compareTo(BigDecimal.valueOf(10000)));
        assertEquals("deposit", transaction1.getType());

        TransactionDTO transaction2 = transactionDTOList.get(1);

        assertNotNull(transaction2.getTransactionId());
        assertNotNull(transaction2.getOriginAccount());
        assertNotNull(transaction2.getOriginAccount().getUser());

        assertTrue(transaction2.getOriginAccount().isStatus());
        assertEquals(0, transaction2.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals("withdrawal", transaction2.getType());

        TransactionDTO transaction3 = transactionDTOList.get(2);

        assertNotNull(transaction3.getTransactionId());
        assertNotNull(transaction3.getOriginAccount());
        assertNotNull(transaction3.getTargetAccount());
        assertNotNull(transaction3.getOriginAccount().getUser());
        assertNotNull(transaction3.getTargetAccount().getUser());

        assertTrue(transaction3.getOriginAccount().isStatus());
        assertEquals("transfer", transaction3.getType());
        assertEquals(0, transaction3.getValue().compareTo(BigDecimal.valueOf(5000)));
    }

    private void mockUser(){
        userDTO.setName("Gaara");
        userDTO.setEmail("Gaara@konaha.com");
        userDTO.setPassword("AldeiaDaAreia");
    }
}