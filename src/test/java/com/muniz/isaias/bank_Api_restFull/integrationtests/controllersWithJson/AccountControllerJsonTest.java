package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithJson;

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
class AccountControllerJsonTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;
    private static AccountDTO accountDTO;
    private static UserDTO userDTO;
    private static RequestSpecification specification;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        accountDTO = new AccountDTO();
        userDTO = new UserDTO();
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

        specification.basePath("bank-api/account");

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", persistedUser.getUserId())
                .when()
                .post("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        AccountDTO createdAccount = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = createdAccount;

        assertNotNull(createdAccount.getAccountId());

        assertEquals(BigDecimal.ZERO, createdAccount.getAccountBalance());
        assertEquals(true, createdAccount.isStatus());
        assertEquals("isaias", createdAccount.getUser().getName());
        assertEquals("isaiasmuniz8@gmail.com", createdAccount.getUser().getEmail());
        assertEquals("adm123", createdAccount.getUser().getPassword());
    }

    @Test
    @Order(2)
    void findAccountById() throws JsonProcessingException {

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", accountDTO.getAccountId())
                .when()
                .get("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        AccountDTO persistedDto = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = persistedDto;

        assertNotNull(persistedDto.getAccountId());

        assertEquals(0, persistedDto.getAccountBalance().compareTo(BigDecimal.ZERO));
        assertEquals(true, persistedDto.isStatus());
        assertEquals("isaias", persistedDto.getUser().getName());
        assertEquals("isaiasmuniz8@gmail.com", persistedDto.getUser().getEmail());
        assertEquals("adm123", persistedDto.getUser().getPassword());
    }

    @Test
    @Order(3)
    void blockAccount() throws JsonProcessingException {
        specification.basePath("bank-api/account/block");
        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", accountDTO.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        AccountDTO persistedDto = objectMapper.readValue(content, AccountDTO.class);
        accountDTO = persistedDto;

        assertNotNull(persistedDto.getAccountId());

        assertFalse(persistedDto.isStatus());
    }

    @Test
    @Order(4)
    void unBlockAccount() throws JsonProcessingException {

        specification.basePath("bank-api/account/unblock");
        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", accountDTO.getAccountId())
                .when()
                .put("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        AccountDTO persistedDto = objectMapper.readValue(content, AccountDTO.class);

        assertNotNull(persistedDto.getAccountId());

        assertTrue(persistedDto.isStatus());
    }

    private void mockUser(){
        userDTO.setName("isaias");
        userDTO.setEmail("isaiasmuniz8@gmail.com");
        userDTO.setPassword("adm123");
    }
}