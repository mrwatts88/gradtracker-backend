package edu.uwm.capstone.controller;

import edu.uwm.capstone.Application;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link MathematicsRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class MathematicsRestControllerComponentTest {

    @Value("${local.server.port}")
    int port;

    @Value("${server.servlet.context-path}")
    String basePath;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }

    private static final double DELTA = 1e-15;

    /**
     * Verify that {@link MathematicsRestController#add(int, int)} correctly adds two values.
     */
    @Test
    public void add() {
            ExtractableResponse<Response> response = given()
                    .when()
                    .get(MathematicsRestController.MATHEMATICS_ADD_PATH, 10, 42)
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
            int result = Integer.parseInt(response.response().body().asString());
            assertEquals("Wrong Result", 52, result);
        }

    /**
     * Verify that {@link MathematicsRestController#subtract(int, int)} correctly subtracts two values.
     */
    @Test
    public void subtract() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(MathematicsRestController.MATHEMATICS_SUBTRACT_PATH, 60, 42)
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
        int result = Integer.parseInt(response.response().body().asString());
        assertEquals("Wrong Result", 18, result);
    }

    /**
     * Verify that {@link MathematicsRestController#multiply(int, int)} correctly multiplies two values.
     */
    @Test
    public void multiply() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(MathematicsRestController.MATHEMATICS_MULTIPLY_PATH, 6, 5)
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
        int result = Integer.parseInt(response.response().body().asString());
        assertEquals("Wrong Result", 30, result);
    }

    /**
     * Verify that {@link MathematicsRestController#divide(int, int)} correctly divides two values.
     */
    @Test
    public void divide() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(MathematicsRestController.MATHEMATICS_DIVIDE_PATH, 100, 4)
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
        int result = Integer.parseInt(response.response().body().asString());
        assertEquals("Wrong Result", 25, result);
    }

    /**
     * Verify that {@link MathematicsRestController#squareRoot(double)} correctly takes square root of given number.
     */
    @Test
    public void squareRoot() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(MathematicsRestController.MATHEMATICS_SQUAREROOT_PATH, 64)
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
        double result = Double.valueOf(response.response().body().asString());
        assertEquals(8, result, DELTA);
    }
}

