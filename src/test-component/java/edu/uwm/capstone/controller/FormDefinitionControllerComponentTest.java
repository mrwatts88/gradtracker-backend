package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.model.FormDefinition;
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
import static edu.uwm.capstone.util.TestDataUtility.formDefWithTestValues;
import static io.restassured.RestAssured.form;
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
public class FormDefinitionControllerComponentTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FormDefinitionDao formDefinitionDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<FormDefinition> formsToCleanup = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(formDefinitionDao);

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
        formsToCleanup.forEach(form -> formDefinitionDao.delete(form.getId()));
        formsToCleanup.clear();
    }

    /**
     * Verify that {@link FormDefinition #create} is working correctly.
     */
    @Test
    public void create() throws Exception {
        FormDefinition formToCreate = formDefWithTestValues();

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formToCreate))
                .when()
                .post(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        FormDefinition receivedFormDef = response.body().as(FormDefinition.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedFormDef.getId());
        assertNotNull(receivedFormDef.getCreatedDate());
        assertEquals(formDefinitionDao.read(receivedFormDef.getId()), receivedFormDef);
        formsToCleanup.add(receivedFormDef);
    }

    /**
     * Verify that {@link FormDefinition #create} is working correctly when an incorrect id is passed in.
     */
    @Test
    public void createPreconditionFailedId() throws Exception {
        FormDefinition formDefinition = formDefWithTestValues();
        formDefinition.setId(randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinition))
                .when()
                .post(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Form definition id should be null"));
    }

    /**
     * Verify that {@link FormDefinition #read} is working correctly when a request for a {@link FormDefinition #id} is made.
     **/
    @Test
    public void readById() {
        FormDefinition formDefinition = formDefWithTestValues();
        formsToCleanup.add(formDefinitionDao.create(formDefinition));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        FormDefinition receivedFormDef = response.body().as(FormDefinition.class);
        assertEquals(formDefinition, receivedFormDef);
    }

    /**
     * Verify that {@link FormDefinition #read} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     **/
    @Test
    public void readByIdNotFound() {
        Long formDefId = randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormDefinitionRestController.FORM_DEF_PATH + formDefId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Form definition with ID: " + formDefId + " not found."));
    }

    /**
     * Verify that {@link FormDefinition #delete} is working correctly when a request for a {@link FormDefinition #id} is made.
     **/
    @Test
    public void deleteById() {
        FormDefinition formDefinition = formDefWithTestValues();
        formDefinitionDao.create(formDefinition);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    /**
     * Verify that {@link FormDefinition #delete} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     **/
    @Test
    public void deleteByIdNotFound() {
        Long formDefId = randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(FormDefinitionRestController.FORM_DEF_PATH + formDefId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not delete form definition " + formDefId + " - record not found."));
    }
}
