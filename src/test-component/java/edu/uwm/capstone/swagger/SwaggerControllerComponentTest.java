package edu.uwm.capstone.swagger;

import edu.uwm.capstone.UnitTestConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import edu.uwm.capstone.Application;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link SwaggerController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SwaggerControllerComponentTest {

    @Value("${local.server.port}")
    int port;

    @Value("${server.servlet.context-path}")
    String basePath;

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);

        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }

    /**
     * Within the spring boot {@link Application} running in memory, use {@link RestAssured} to exercise
     * the REST endpoint provided at {@link SwaggerController#SWAGGER_UI} and verify that it returns content.
     */
    @Test
    public void swaggerui() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(SwaggerController.SWAGGER_UI)
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
    }

}
