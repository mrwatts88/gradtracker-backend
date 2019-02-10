package edu.uwm.capstone.controller;

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
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link PalindromeRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PalindromeRestControllerComponentTest {

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
     * Verify that {@link PalindromeRestController#isPalindrome} correctly identifies known palindromes.
     */
    @Test
    public void verifyKnownPalindromes() {
        String[] knownPalindromes = {"redivider", "noon", "civic", "radar", "level", "rotor", "kayak", "reviver",
                "racecar", "redder", "madam", "refer"};
        for (String value : knownPalindromes) {
            ExtractableResponse<Response> response = given()
                    .when()
                    .get(PalindromeRestController.PALINDROME_PATH, value)
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();
            assertNotNull(response);
            assertNotNull(response.response());
            assertNotNull(response.response().body());
            assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
            Boolean isPalindrome = Boolean.valueOf(response.response().body().asString());
            assertTrue(value + " is a known palindrome", isPalindrome);
        }
    }

    /**
     * Verify that {@link PalindromeRestController#isPalindrome} correctly identifies known palindromes.
     */
    @Test
    public void verifyInvalidPalindromes() {
        String[] knownPalindromes = {"rdivder", "nzoont", "cuvic", "roder", "livul", "rater", "koyuk", "ravevir",
                "rececar", "rudder", "modim", "rifurs"};
        for (String value : knownPalindromes) {
            ExtractableResponse<Response> response = given()
                    .when()
                    .get(PalindromeRestController.PALINDROME_PATH, value)
                    .then()
                    .statusCode(HttpStatus.OK.value()).extract();
            assertNotNull(response);
            assertNotNull(response.response());
            assertNotNull(response.response().body());
            assertTrue(StringUtils.isNotBlank(response.response().body().asString()));
            Boolean isPalindrome = Boolean.valueOf(response.response().body().asString());
            assertFalse(value + " is an invalid palindrome", isPalindrome);
        }
    }

}
