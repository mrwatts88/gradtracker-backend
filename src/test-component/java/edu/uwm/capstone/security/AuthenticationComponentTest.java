package edu.uwm.capstone.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<User> usersToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(userDao);
        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }

    @After
    public void teardown() {
        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
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

        String userJSON = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();

        assertNotNull(userJSON);

        try {
            User userSubject = new ObjectMapper().readValue(userJSON, User.class);

            assertEquals(DEFAULT_USER.getFirstName(), userSubject.getFirstName());
            assertEquals(DEFAULT_USER.getLastName(), userSubject.getLastName());
            assertEquals(DEFAULT_USER.getEmail(), userSubject.getEmail());
            assertEquals(DEFAULT_USER.getPantherId(), userSubject.getPantherId());

            // TODO uncomment these lines once UserDao and RoleDao are done and default role, "Admin" is persisted with all authorities
//            assertEquals(DEFAULT_USER.getRoleNames(), userSubject.getRoleNames());
//            assertEquals(Sets.newHashSet(Authorities.values()), userSubject.getAuthorities());

        } catch (IOException e) {
            fail("Mapping JWT subject to User class failed");
            e.getStackTrace();
        }
    }

    @Test
    public void existentUserCanGetToken() {
        User user = userWithTestValues();
        String credentials = "{ \"email\" : \"" + user.getEmail() + "\", \"password\" : \"" + user.getPassword() + "\" }";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.create(user);
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

        String userJSON = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();

        assertNotNull(userJSON);

        try {
            User userSubject = new ObjectMapper().readValue(userJSON, User.class);

            assertEquals(user.getFirstName(), userSubject.getFirstName());
            assertEquals(user.getLastName(), userSubject.getLastName());
            assertEquals(user.getEmail(), userSubject.getEmail());
            assertEquals(user.getPantherId(), userSubject.getPantherId());
            assertTrue(userSubject.getRoleNames().isEmpty());
            assertTrue(userSubject.getAuthorities().isEmpty());

        } catch (IOException e) {
            fail("Mapping JWT subject to User class failed");
            e.getStackTrace();
        }
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
}
