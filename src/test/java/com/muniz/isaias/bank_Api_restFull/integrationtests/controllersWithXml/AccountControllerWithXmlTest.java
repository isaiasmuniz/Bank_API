package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muniz.isaias.bank_Api_restFull.integrationtests.AbstractIntegration;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.AccountDTO;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.UserDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerWithXmlTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;
    private static AccountDTO accountDTO;
    private static UserDTO userDTO;
    private static RequestSpecification specification;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        accountDTO = new AccountDTO();
        userDTO =  new UserDTO();
    }

    @Test
    @Order(1)
    void createAccount() throws JsonProcessingException {
        mockUser();

        specification = new RequestSpecBuilder().addHeader("origin", "http://localhost:8888")
                .setBasePath("bank-api/user")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var createUser = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(userDTO)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO createdUser = objectMapper.readValue(createUser, UserDTO.class);
        userDTO = createdUser;

        specification.basePath("bank-api/account");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", createdUser.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO createdAccount = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = createdAccount;

        assertNotNull(createdAccount.getAccountId());

        assertTrue(createdAccount.isStatus());
        assertEquals(BigDecimal.ZERO, createdAccount.getAccountBalance());
        assertEquals("Rebeca", createdAccount.getUser().getName());
        assertEquals("Rebeca@email.com", createdAccount.getUser().getEmail());
        assertEquals("dockerCompose", createdAccount.getUser().getPassword());
    }

    @Test
    @Order(2)
    void findAccountById() throws JsonProcessingException {

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", accountDTO.getAccountId())
                .when()
                .get("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO createdAccount = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = createdAccount;

        assertNotNull(createdAccount.getAccountId());

        assertTrue(createdAccount.isStatus());
        assertEquals(0, createdAccount.getAccountBalance().compareTo(BigDecimal.ZERO));
        assertEquals("Rebeca", createdAccount.getUser().getName());
        assertEquals("Rebeca@email.com", createdAccount.getUser().getEmail());
        assertEquals("dockerCompose", createdAccount.getUser().getPassword());
    }

    @Test
    @Order(3)
    void blockAccount() throws JsonProcessingException {

        specification.basePath("bank-api/account/block");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", accountDTO.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO createdAccount = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = createdAccount;

        assertNotNull(createdAccount.getAccountId());

        assertFalse(createdAccount.isStatus());
    }

    @Test
    @Order(4)
    void unBlockAccount() throws JsonProcessingException {

        specification.basePath("bank-api/account/unblock");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", accountDTO.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        AccountDTO createdAccount = objectMapper.readValue(content, AccountDTO.class);

        assertNotNull(createdAccount.getAccountId());

        assertTrue(createdAccount.isStatus());
    }

    private void mockUser(){
        userDTO.setName("Rebeca");
        userDTO.setEmail("Rebeca@email.com");
        userDTO.setPassword("dockerCompose");
    }
}