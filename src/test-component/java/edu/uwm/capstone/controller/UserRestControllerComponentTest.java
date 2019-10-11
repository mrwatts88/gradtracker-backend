package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.User;
import io.restassured.RestAssured;
import io.restassured.http.Header;
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

import java.util.ArrayList;
import java.util.List;

import static edu.uwm.capstone.security.SecurityConstants.AUTHENTICATE_URL;
import static edu.uwm.capstone.security.SecurityConstants.DEFAULT_USER_CREDENTIALS;
import static edu.uwm.capstone.util.TestDataUtility.randomLong;
import static edu.uwm.capstone.util.TestDataUtility.userWithTestValues;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link UserRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserRestControllerComponentTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDao userDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<User> usersToCleanup = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(userDao);

        RestAssured.port = port;
        RestAssured.basePath = basePath;

        // get authorization token if it's null
        if (authorizationToken == null) {
            ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(DEFAULT_USER_CREDENTIALS)
                    .when()
                    .post(AUTHENTICATE_URL)
                    .then().log().ifValidationFails()
                    .statusCode(HttpStatus.OK.value()).extract();

            authorizationToken = response.header("Authorization");
        }
    }

    @After
    public void teardown() {
        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
    }

    @Test
    public void create() throws Exception {
        User userToCreate = userWithTestValues();

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(userToCreate))
                .when()
                .post(UserRestController.USER_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        User receivedUser = response.body().as(User.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedUser.getId());
        assertNotEquals(userToCreate.getPassword(), receivedUser.getPassword());
        assertNotNull(receivedUser.getCreatedDate());
        assertEquals(userDao.read(receivedUser.getId()), receivedUser);
        usersToCleanup.add(receivedUser);
    }

    @Test
    public void createPreconditionFailedId() throws Exception {
        User user = userWithTestValues();
        user.setId(randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(user))
                .when()
                .post(UserRestController.USER_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("User ID must be null"));
    }

    @Test
    public void createPreconditionFailedEmail() throws Exception {
        User user = userWithTestValues();
        user.setEmail(null);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(user))
                .when()
                .post(UserRestController.USER_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("User email must not be null"));
    }

    @Test
    public void createPreconditionFailedPassword() throws Exception {
        User user = userWithTestValues();
        user.setPassword(null);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(user))
                .when()
                .post(UserRestController.USER_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("User password must not be null"));
    }

    @Test
    public void update() throws Exception {
        User user = userWithTestValues();
        usersToCleanup.add(userDao.create(user));

        User userToUpdate = userWithTestValues();

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(userToUpdate))
                .when()
                .put(UserRestController.USER_PATH + user.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        User verifyUser = userDao.read(user.getId());
        assertNotNull(verifyUser.getUpdatedDate());
        assertNotEquals(userToUpdate.getPassword(), verifyUser.getPassword());
        assertEquals(userToUpdate.getFirstName(), verifyUser.getFirstName());
        assertEquals(userToUpdate.getLastName(), verifyUser.getLastName());
        assertEquals(userToUpdate.getEmail(), verifyUser.getEmail());
        assertEquals(userToUpdate.getPantherId(), verifyUser.getPantherId());
    }

    @Test
    public void updateNotFound() throws Exception {
        User userToUpdate = userWithTestValues();
        userToUpdate.setId(randomLong());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(userToUpdate))
                .when()
                .put(UserRestController.USER_PATH + userToUpdate.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not update User " + userToUpdate.getId() + " - record not found."));
    }

    @Test
    public void updatePreconditionFailedEmail() throws Exception {
        User user = userWithTestValues();
        usersToCleanup.add(userDao.create(user));

        User userToUpdate = userWithTestValues();
        userToUpdate.setEmail(null);

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(userToUpdate))
                .when()
                .put(UserRestController.USER_PATH + user.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("User email must not be null"));
    }

    @Test
    public void updatePreconditionFailedPassword() throws Exception {
        User user = userWithTestValues();
        usersToCleanup.add(userDao.create(user));

        User userToUpdate = userWithTestValues();
        userToUpdate.setPassword(null);

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(userToUpdate))
                .when()
                .put(UserRestController.USER_PATH + user.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("User password must not be null"));
    }

    @Test
    public void readById() {
        User user = userWithTestValues();
        usersToCleanup.add(userDao.create(user));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(UserRestController.USER_PATH + user.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        User receivedUser = response.body().as(User.class);
        assertEquals(user, receivedUser);
    }

    @Test
    public void readByIdNotFound() {
        Long userId = randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(UserRestController.USER_PATH + userId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("User with ID: " + userId + " not found."));
    }

    @Test
    public void deleteById() {
        User user = userWithTestValues();
        userDao.create(user);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(UserRestController.USER_PATH + user.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    @Test
    public void deleteByIdNotFound() {
        Long userId = randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(UserRestController.USER_PATH + userId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not delete User " + userId + " - record not found."));
    }
}
