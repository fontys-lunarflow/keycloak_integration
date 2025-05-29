package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class GreetingResourceTest {
    
    @Test
    void testPublicStatusEndpoint() {
        given()
          .when().get("/api/system/status")
          .then()
             .statusCode(200)
             .body("status", is("UP"))
             .body("version", is("1.0.0"))
             .body("timestamp", notNullValue());
    }
    
    @Test
    void testPublicTestSecurityEndpoint() {
        given()
          .when().get("/test-security/public")
          .then()
             .statusCode(200)
             .body("message", is("This endpoint is public"))
             .body("authenticated", is(false));
    }
}