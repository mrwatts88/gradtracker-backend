package edu.uwm.capstone.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.UserService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.uwm.capstone.security.SecurityConstants.*;
import static edu.uwm.capstone.util.TestDataUtility.userWithTestValues;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthenticationComponentTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private UserService userService;

    private List<User> usersToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(userService);
        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }

    @After
    public void teardown() {
        usersToCleanup.forEach(user -> userService.delete(user.getId()));
        usersToCleanup.clear();
    }

    @Test
    public void defaultUserCanGetToken() {
        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(DEFAULT_USER_CREDENTIALS)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization").replaceFirst("Bearer ", "").trim();
        assertNotNull(token);
    }

    @Test
    public void existentUserCanGetToken() {
        User user = userWithTestValues();
        String credentials = "{ \"email\" : \"" + user.getEmail() + "\", \"password\" : \"" + user.getPassword() + "\" }";
        userService.create(user);
        usersToCleanup.add(user);

        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(credentials)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization").replaceFirst("Bearer ", "").trim();
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

    @Test
    public void defaultUserJWTClaims() {
        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(DEFAULT_USER_CREDENTIALS)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization").replaceFirst("Bearer ", "").trim();
        assertNotNull(token);

        Map<String, Claim> claimsMap = JWT.decode(token).getClaims();
        assertEquals(claimsMap.get(JWT_CLAIM_FIRST_NAME).asString(), DEFAULT_USER_FIRST_NAME);
        assertEquals(claimsMap.get(JWT_CLAIM_LAST_NAME).asString(), DEFAULT_USER_LAST_NAME);
        assertEquals(claimsMap.get(JWT_CLAIM_PANTHER_ID).asString(), DEFAULT_USER_PANTHER_ID);
        assertEquals(claimsMap.get(JWT_CLAIM_EMAIL).asString(), DEFAULT_USER_EMAIL);
    }

    @Test
    public void existentUserJWTClaims() {
        User user = userWithTestValues();
        String credentials = "{ \"email\" : \"" + user.getEmail() + "\", \"password\" : \"" + user.getPassword() + "\" }";
        userService.create(user);
        usersToCleanup.add(user);

        // exercise authentication endpoint
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(credentials)
                .when()
                .post(AUTHENTICATE_URL)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        String token = response.header("Authorization").replaceFirst("Bearer ", "").trim();
        assertNotNull(token);

        Map<String, Claim> claimsMap = JWT.decode(token).getClaims();
        assertEquals(claimsMap.get(JWT_CLAIM_ID).asLong(), user.getId());
        assertEquals(claimsMap.get(JWT_CLAIM_FIRST_NAME).asString(), user.getFirstName());
        assertEquals(claimsMap.get(JWT_CLAIM_LAST_NAME).asString(), user.getLastName());
        assertEquals(claimsMap.get(JWT_CLAIM_PANTHER_ID).asString(), user.getPantherId());
        assertEquals(claimsMap.get(JWT_CLAIM_EMAIL).asString(), user.getEmail());
    }
}
