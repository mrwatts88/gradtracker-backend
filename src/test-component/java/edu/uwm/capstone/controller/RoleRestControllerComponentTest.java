package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.util.TestDataUtility;
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

import static edu.uwm.capstone.security.SecurityConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link RoleRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoleRestControllerComponentTest {
    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoleDao roleDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<Role> rolesToCleanup = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(roleDao);

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
        rolesToCleanup.forEach(role -> roleDao.delete(role.getId()));
        rolesToCleanup.clear();
    }

    @Test
    public void create() throws Exception {
        Role roleToCreate = TestDataUtility.roleWithTestValues();

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(roleToCreate))
                .when()
                .post(RoleRestController.ROLE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Role receivedRole = response.body().as(Role.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedRole.getId());
        assertNotNull(receivedRole.getCreatedDate());
        assertEquals(roleDao.read(receivedRole.getId()), receivedRole);
        rolesToCleanup.add(receivedRole);
    }

    @Test
    public void createPreconditionFailedId() throws Exception {
        Role role = TestDataUtility.roleWithTestValues();
        role.setId(TestDataUtility.randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(role))
                .when()
                .post(RoleRestController.ROLE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Role ID must be null"));
    }


    @Test
    public void createPreconditionFailedExistentId() throws Exception {
        Role role = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(roleDao.create(role));

        Role createRole = TestDataUtility.roleWithTestValues();
        createRole.setId(role.getId());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createRole))
                .when()
                .post(RoleRestController.ROLE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Role ID must be null"));
    }

    //TODO: find the way to fix the update method and implement its tests.
    @Test
    public void update() throws Exception {
        Role role = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(roleDao.create(role));

        Role roleToUpdate = TestDataUtility.roleWithTestValues();
        roleToUpdate.setId(role.getId());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(roleToUpdate))
                .when()
                .put(RoleRestController.ROLE_PATH + roleToUpdate.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Role verifyRole = roleDao.read(role.getId());
        assertNotNull(verifyRole.getUpdatedDate());
        assertEquals(roleToUpdate.getName(), verifyRole.getName());
        assertEquals(roleToUpdate.getDescription(), verifyRole.getDescription());
        assertEquals(roleToUpdate.getAuthorities(), verifyRole.getAuthorities());
    }

    @Test
    public void updateNotFound() throws Exception {
        Role roleToUpdate = TestDataUtility.roleWithTestValues();
        roleToUpdate.setId(TestDataUtility.randomLong());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(roleToUpdate))
                .when()
                .put(RoleRestController.ROLE_PATH + roleToUpdate.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not update Role " + roleToUpdate.getId() + " - record not found."));
    }

    @Test
    public void readById() {
        Role role = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(roleDao.create(role));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(RoleRestController.ROLE_PATH + role.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Role receivedRole = response.body().as(Role.class);
        assertEquals(role, receivedRole);
    }

    @Test
    public void readByName() {
        Role role = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(roleDao.create(role));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(RoleRestController.ROLE_PATH + "name/" + role.getName())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Role receivedRole = response.body().as(Role.class);
        assertEquals(role, receivedRole);
    }

    @Test
    public void readByIdNotFound() {
        Long roleId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(RoleRestController.ROLE_PATH + roleId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Role with ID: " + roleId + " not found."));
    }

    @Test
    public void readByNameNotFound() {
        String name = TestDataUtility.randomAlphabetic(9);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(RoleRestController.ROLE_PATH + "name/" + name)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Role with name: " + name + " not found."));
    }

    @Test
    public void deleteById() {
        Role role = TestDataUtility.roleWithTestValues();
        roleDao.create(role);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(RoleRestController.ROLE_PATH + role.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    @Test
    public void deleteByIdNotFound() {
        Long roleId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(RoleRestController.ROLE_PATH + roleId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not delete Role " + roleId + " - record not found."));
    }
}
