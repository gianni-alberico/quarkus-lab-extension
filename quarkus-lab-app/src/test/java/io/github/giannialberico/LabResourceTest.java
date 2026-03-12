package io.github.giannialberico;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LabResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/lab/reflect")
          .then()
             .statusCode(200)
             .body(is("Invoked ReflectiveService.hello(): Hello from Reflective Service"));
    }

}