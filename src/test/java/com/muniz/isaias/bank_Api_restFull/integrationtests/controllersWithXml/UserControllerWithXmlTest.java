package com.muniz.isaias.bank_Api_restFull.integrationtests.controllersWithXml;

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
class UserControllerWithXmlTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;
    private static UserDTO userDTO;
    private static RequestSpecification specification;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        userDTO = new UserDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {

        mockUser();
        specification = new RequestSpecBuilder().addHeader("origin", "http://localhost:8888")
                .setBasePath("bank-api/user")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(userDTO)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();
        UserDTO createdUser = objectMapper.readValue(content, UserDTO.class);
        userDTO = createdUser;

        assertNotNull(createdUser.getUserId());

        assertEquals("Isaias", createdUser.getName());
        assertEquals("isaias@email.com", createdUser.getEmail());
        assertEquals("adm123", createdUser.getPassword());
    }

    @Test
    @Order(2)
    void update() throws JsonProcessingException {

        userDTO.setName("Douglas");
        userDTO.setEmail("Douglas@email.com");

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .body(userDTO)
                .when()
                .put()
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO createdUser = objectMapper.readValue(content, UserDTO.class);
        userDTO = createdUser;

        assertNotNull(createdUser.getUserId());

        assertEquals("Douglas", createdUser.getName());
        assertEquals("Douglas@email.com", createdUser.getEmail());
        assertEquals("adm123", createdUser.getPassword());
    }

    @Test
    @Order(3)
    void findUserById() throws JsonProcessingException {

        var content = given(specification).contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", userDTO.getUserId())
                .when()
                .get("{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body().asString();

        UserDTO createdUser = objectMapper.readValue(content, UserDTO.class);

        assertNotNull(createdUser.getUserId());

        assertEquals("Douglas", createdUser.getName());
        assertEquals("Douglas@email.com", createdUser.getEmail());
        assertEquals("adm123", createdUser.getPassword());
    }

    private void mockUser(){
        userDTO.setName("Isaias");
        userDTO.setEmail("isaias@email.com");
        userDTO.setPassword("adm123");
    }
}