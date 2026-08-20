package com.muniz.isaias.bank_Api_restFull.integrationtests.swagger;

import com.muniz.isaias.bank_Api_restFull.integrationtests.AbstractIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerTest extends AbstractIntegration {

    @Test
    void shouldDisplayUIPage(){
        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(8888)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("Swagger UI"));
    }
}
