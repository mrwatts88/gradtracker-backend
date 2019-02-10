package edu.uwm.capstone.controller;

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
import edu.uwm.capstone.helper.TestHelper;
import edu.uwm.capstone.util.Concatenation;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link ConcatenationRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ConcatenationRestControllerComponentTest {

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
     * the REST endpoint provided at {@link ConcatenationRestController#CONCATENATE_PATH} and verify that it returns content.
     */
    @Test
    public void concatenate() {
        ExtractableResponse<Response> response = given()
                .when()
                .get(ConcatenationRestController.CONCATENATE_PATH,"one", "two")
                .then()
                .statusCode(HttpStatus.OK.value()).extract();
        assertNotNull(response);
        assertNotNull(response.response());
        assertNotNull(response.response().body());
        assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
        assertEquals("onetwo", response.response().body().asString());
    }

    /**
     * This test uses our {@link TestHelper} to get random string values for use in verifying that
     * {@link Concatenation#concatenate} correctly concatenates the values that are provided.
     * The test is performed a random number of times based on the repeatTheTest provided by
     * {@link TestHelper#randomInteger} and uses random strings provided by {@link TestHelper#randomAlphabetic}
     * and {@link TestHelper#randomAlphanumeric} which introduce randomness into the validations that are performed.
     */
    @Test
    public void verifyRandomConcatenations() {
        Integer repeatTheTest = TestHelper.randomInteger(15, 50);
        Integer firstStringLength;
        Integer secondStringLength;
        Integer concatenatedStringLength;
        String firstString;
        String secondString;
        String concatenatedString;

        for (int i=0; i <= repeatTheTest; i++) {
            // get random sizes for our two strings
            firstStringLength = TestHelper.randomInteger(30, 90);
            secondStringLength = TestHelper.randomInteger(30, 90);
            concatenatedStringLength = firstStringLength + secondStringLength;

            // get random values based on the random sizes for our two strings
            firstString = TestHelper.randomAlphabetic(firstStringLength);
            secondString = TestHelper.randomAlphanumeric(secondStringLength);

            // concatenate our two randomly sized random value strings
            ExtractableResponse<Response> response = given()
                    .when()
                    .get(ConcatenationRestController.CONCATENATE_PATH,firstString, secondString)
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();
            assertNotNull(response);
            assertNotNull(response.response());
            assertNotNull(response.response().body());
            assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
            concatenatedString = response.response().body().asString();

            // verify that the concatenated string length
            assertEquals("Invalid length of the concatenated string",
                    concatenatedStringLength.intValue(), concatenatedString.length());

            // verify that the concatenated string begins with the first string and ends with the second string
            assertTrue(concatenatedString.startsWith(firstString));
            assertTrue(concatenatedString.endsWith(secondString));
        }
    }

}
