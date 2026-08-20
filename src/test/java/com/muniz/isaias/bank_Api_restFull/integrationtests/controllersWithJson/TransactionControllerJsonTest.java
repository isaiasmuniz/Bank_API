package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithJson;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionControllerJsonTest extends AbstractIntegration {

    private static TransactionDTO transactionDTO;
    private static ObjectMapper objectMapper;
    private static RequestSpecification specification;
    private static AccountDTO accountDTO;
    private static UserDTO userDTO;

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
        var targetUser = userDTO;

        specification = new RequestSpecBuilder().addHeader("origin", "http://localhost:8888")
                .setBasePath("bank-api/user")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var createUser = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(userDTO)
                        .when()
                        .post()
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().body().asString();
        UserDTO persistedUser = objectMapper.readValue(createUser, UserDTO.class);
        userDTO = persistedUser;


        var createTargetUser = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(targetUser)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        UserDTO persistedTargetUser = objectMapper.readValue(createTargetUser, UserDTO.class);

        specification.basePath("bank-api/account");

        var createAccount = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", userDTO.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        AccountDTO peristedAccount = objectMapper.readValue(createAccount, AccountDTO.class);
        peristedAccount.setUser(persistedUser);
        transactionDTO.setOriginAccount(peristedAccount);

        var createTargetAccount = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", persistedTargetUser.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();
        AccountDTO persistedTargetAccount = objectMapper.readValue(createTargetAccount, AccountDTO.class);
        persistedTargetAccount.setUser(persistedTargetUser);
        transactionDTO.setTargetAccount(persistedTargetAccount);

        specification.basePath("bank-api/transaction/deposit");
        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(transactionDTO1)
                .pathParams("id", peristedAccount.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        transactionDTO1 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO1.getTransactionId());
        assertNotNull(transactionDTO1.getOriginAccount());
        assertNotNull(transactionDTO1.getOriginAccount().getUser());

        assertTrue(transactionDTO1.getOriginAccount().isStatus());
        assertEquals(0, transactionDTO1.getValue().compareTo(BigDecimal.valueOf(10000)));
        assertEquals(0, transactionDTO1.getOriginAccount().getAccountBalance().compareTo(BigDecimal.valueOf(10000)));
        assertEquals("deposit", transactionDTO1.getType());
    }

    @Test
    @Order(2)
    void withdrawal() throws JsonProcessingException {

        TransactionDTO transactionDTO2 = new TransactionDTO("withdrawal", BigDecimal.valueOf(5000));
        specification.basePath("bank-api/transaction/withdrawal");

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(transactionDTO2)
                .pathParams("id", transactionDTO.getOriginAccount().getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        transactionDTO2 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO2.getTransactionId());
        assertNotNull(transactionDTO2.getOriginAccount());
        assertNotNull(transactionDTO2.getOriginAccount().getUser());

        assertTrue(transactionDTO2.getOriginAccount().isStatus());
        assertEquals(0, transactionDTO2.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals(0, transactionDTO2.getOriginAccount().getAccountBalance().compareTo(BigDecimal.valueOf(5000)));
        assertEquals("withdrawal", transactionDTO2.getType());
    }

    @Test
    @Order(3)
    void bankTransfer() throws JsonProcessingException {


        specification.basePath("bank-api/transaction/transfer");
        TransactionDTO transactionDTO3 = new TransactionDTO("transfer", BigDecimal.valueOf(5000));

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(transactionDTO3)
                .pathParams("id", transactionDTO.getOriginAccount().getAccountId(), "targetId", transactionDTO.getTargetAccount().getAccountId())
                .when()
                .put("/{id}/{targetId}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        transactionDTO3 = objectMapper.readValue(content, TransactionDTO.class);

        assertNotNull(transactionDTO3.getTransactionId());
        assertNotNull(transactionDTO3.getOriginAccount());
        assertNotNull(transactionDTO3.getTargetAccount());
        assertNotNull(transactionDTO3.getOriginAccount().getUser());
        assertNotNull(transactionDTO3.getTargetAccount().getUser());

        assertTrue(transactionDTO3.getOriginAccount().isStatus());
        assertEquals("transfer", transactionDTO3.getType());
        assertEquals(0, transactionDTO3.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals(0, transactionDTO3.getOriginAccount().getAccountBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, transactionDTO3.getTargetAccount().getAccountBalance().compareTo(BigDecimal.valueOf(5000)));
    }

    @Test
    @Order(4)
    void viewHistory() throws JsonProcessingException {

        specification.basePath("bank-api/transaction");
        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", transactionDTO.getOriginAccount().getAccountId())
                .queryParams("page", 0, "size", 3, "direction", "asc")
                .when()
                .get("/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        WrapperTransactionDto wrapper = objectMapper.readValue(content, WrapperTransactionDto.class);
        List<TransactionDTO> transactionDTOList = wrapper.getEmbedded().getTransactionDTOList();

        assertEquals(3, transactionDTOList.size());

        TransactionDTO firstTransaction = transactionDTOList.get(0);

        assertNotNull(firstTransaction.getTransactionId());
        assertNotNull(firstTransaction.getOriginAccount());
        assertNotNull(firstTransaction.getOriginAccount().getUser());

        assertTrue(firstTransaction.getOriginAccount().isStatus());
        assertEquals(0, firstTransaction.getValue().compareTo(BigDecimal.valueOf(10000)));
        assertEquals("deposit", firstTransaction.getType());

        TransactionDTO secondTransaction = transactionDTOList.get(1);

        assertNotNull(secondTransaction.getTransactionId());
        assertNotNull(secondTransaction.getOriginAccount());
        assertNotNull(secondTransaction.getOriginAccount().getUser());

        assertTrue(secondTransaction.getOriginAccount().isStatus());
        assertEquals(0, secondTransaction.getValue().compareTo(BigDecimal.valueOf(5000)));
        assertEquals("withdrawal", secondTransaction.getType());

        TransactionDTO thirdTransaction = transactionDTOList.get(2);

        assertNotNull(thirdTransaction.getTransactionId());
        assertNotNull(thirdTransaction.getOriginAccount());
        assertNotNull(thirdTransaction.getTargetAccount());
        assertNotNull(thirdTransaction.getOriginAccount().getUser());
        assertNotNull(thirdTransaction.getTargetAccount().getUser());

        assertTrue(thirdTransaction.getOriginAccount().isStatus());
        assertEquals("transfer", thirdTransaction.getType());
        assertEquals(0, thirdTransaction.getValue().compareTo(BigDecimal.valueOf(5000)));
    }

    private void mockUser(){
        userDTO.setName("samanta");
        userDTO.setEmail("samanta@gmail.com");
        userDTO.setPassword("adm123");
    }
}