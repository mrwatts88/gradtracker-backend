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
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import com.auth0.jwt.JWT;


import static edu.uwm.capstone.security.SecurityConstants.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

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
    public void existentUserCanGetToken() throws Exception {
        String credentials = new JSONObject()
                .put("email", DEFAULT_USER_EMAIL)
                .put("password", DEFAULT_USER_PASSWORD)
                .toString();

        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(credentials)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization");
        assertNotNull(token);
    }

    @Test
    public void nonExistentUserCannotGetToken() throws Exception {
        String credentials = new JSONObject()
                .put("email", "junk")
                .put("password", "junk")
                .toString();

        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(credentials)
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
