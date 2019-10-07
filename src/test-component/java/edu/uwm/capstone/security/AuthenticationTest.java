package edu.uwm.capstone.security;

import edu.uwm.capstone.Application;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static edu.uwm.capstone.security.SecurityConstants.AUTHENTICATE_URL;
import static edu.uwm.capstone.security.SecurityConstants.DEFAULT_USER_CREDENTIALS;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthenticationTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);

        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }

    @After
    public void teardown() {

    }

    @Test
    public void existentUserCanGetToken() {
        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(DEFAULT_USER_CREDENTIALS)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization");
        assertNotNull(token);
    }

    @Test
    public void nonExistentUserCannotGetToken() {
        String nonExistentUser = "{ \"email\" : \"junk\", \"password\" : \"junk\" }";

        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(nonExistentUser)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.FORBIDDEN.value()).extract();

        String token = response.header("Authorization");
        assertNull(token);
    }

    // TODO check that jwt encoding is correct/contains correct claims

//    public static void decodeJWT(String jwt) {
//        System.out.println(JWT.decode(jwt));
        //This line will throw an exception if it is not a signed JWS (as expected)
//        Claims claims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
//                .parseClaimsJws(jwt).getBody();
//        return claims;
//    }

}
