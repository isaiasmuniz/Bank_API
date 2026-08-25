package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muniz.isaias.bank_Api_restFull.integrationtests.AbstractIntegration;
import com.muniz.isaias.bank_Api_restFull.integrationtests.dto.UserDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerJsonTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;
    private static UserDTO dto;
    private static RequestSpecification specification;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        dto = new UserDTO();
    }

    @Test
    @Order(1)
    void createUserTest() throws JsonProcessingException {
        mockUser();
        specification = new RequestSpecBuilder().addHeader("origin","http://localhost:8888")
                .setBasePath("bank-api/user")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO createdUser = objectMapper.readValue(content, UserDTO.class);
        dto = createdUser;

        assertNotNull(createdUser.getUserId());

        assertEquals("Lucas", createdUser.getName());
        assertEquals("admin123", createdUser.getPassword());
        assertEquals("isaiasmuniz8@gmail.com", createdUser.getEmail());
    }

    @Test
    @Order(2)
    void findUserByIdTest() throws JsonProcessingException {

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", dto.getUserId())
                .when()
                .get("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        UserDTO createdUser = objectMapper.readValue(content, UserDTO.class);
        dto = createdUser;

        assertNotNull(createdUser.getUserId());

        assertEquals("Lucas", createdUser.getName());
        assertEquals("admin123", createdUser.getPassword());
        assertEquals("isaiasmuniz8@gmail.com", createdUser.getEmail());
    }

    @Test
    @Order(3)
    void updateUserTest(){
        dto.setName("Rafa");
        dto.setEmail("Rafa@gmail.com");
        dto.setPassword("Abobrinha");

        var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put()
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().asString();

        assertNotNull(dto.getUserId());

        assertEquals("Rafa", dto.getName());
        assertEquals("Abobrinha", dto.getPassword());
        assertEquals("Rafa@gmail.com", dto.getEmail());
    }

    private void mockUser(){
        dto.setName("Lucas");
        dto.setPassword("admin123");
        dto.setEmail("isaiasmuniz8@gmail.com");
    }
}
