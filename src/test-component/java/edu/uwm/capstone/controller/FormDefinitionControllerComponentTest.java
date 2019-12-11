package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.helper.DefaultEntities;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.uwm.capstone.security.SecurityConstants.AUTHENTICATE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link FormDefinitionRestController} are working correctly.
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
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

            Role defaultAdminRole = DefaultEntities.getDefaultAdminRole();
            User defaultUser = DefaultEntities.getDefaultUser();

            String creds = "{ \"email\" : \"" + defaultUser.getEmail() + "\", \"password\" : \"" + defaultUser.getPassword() + "\" }";

            roleDao.create(defaultAdminRole);
            defaultUser.setPassword(passwordEncoder.encode(defaultUser.getPassword()));
            userDao.create(defaultUser);

            ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(creds)
                    .when()
                    .post(AUTHENTICATE_URL)
                    .then().log().ifValidationFails()
                    .statusCode(HttpStatus.OK.value()).extract();

            authorizationToken = response.header("Authorization");

            userDao.delete(defaultUser.getId());
            roleDao.delete(defaultAdminRole.getId());
        }
    }

    @After
    public void teardown() {
        formsToCleanup.forEach(form -> formDefinitionDao.delete(form.getId()));
        formsToCleanup.clear();
    }

    /**
     * Verify that {@link FormDefinitionRestController#create} is working correctly.
     */
    @Test
    public void create() throws Exception {
        FormDefinition formToCreate = TestDataUtility.formDefWithTestValues();

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
     * Verify that {@link FormDefinitionRestController#create} is working correctly when an incorrect id is passed in.
     */
    @Test
    public void createPreconditionFailedId() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinition.setId(TestDataUtility.randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinition))
                .when()
                .post(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .body("message", equalTo("Form definition id should be null"));
    }

    /**
     * Verify that {@link FormDefinitionRestController#create} is working correctly when a null name is passed in.
     */
    @Test
    public void createPreconditionFailedName() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinition.setName(null);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinition))
                .when()
                .post(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .body("message", equalTo("Form definition name cannot be null"));
    }

    /**
     * Verify that {@link FormDefinitionRestController#create} is working correctly
     * when an empty list of field definitions is passed in.
     */
    @Test
    public void createPreconditionFailedEmptyFieldDefinitions() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinition.setFieldDefs(Collections.emptyList());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinition))
                .when()
                .post(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .body("message", equalTo("Form definition must have at least one field definition"));
    }

    /**
     * Verify that {@link FormDefinitionRestController#update} is working correctly.
     */
    @Test
    public void update() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formsToCleanup.add(formDefinitionDao.create(formDefinition));

        FormDefinition formDefinitionToUpdate = TestDataUtility.formDefWithTestValues();

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(formDefinitionToUpdate))
                .when()
                .put(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        FormDefinition verifyFormDefinition = formDefinitionDao.read(formDefinition.getId());
        assertNotNull(verifyFormDefinition.getUpdatedDate());
        assertNotEquals(formDefinition, verifyFormDefinition);
    }

    /**
     * Verify that {@link FormDefinitionRestController#create} is working correctly when an incorrect id is passed in.
     */
    @Test
    public void updateNotFound() throws Exception {
        FormDefinition formDefinitionToUpdate = TestDataUtility.formDefWithTestValues();
        formDefinitionToUpdate.setId(TestDataUtility.randomLong());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(formDefinitionToUpdate))
                .when()
                .put(FormDefinitionRestController.FORM_DEF_PATH + formDefinitionToUpdate.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not update form definition " + formDefinitionToUpdate.getId() + " - record not found."));
    }

    /**
     * Verify that {@link FormDefinitionRestController#update} is working correctly when a null name is passed in.
     */
    @Test
    public void updatePreconditionFailedName() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formsToCleanup.add(formDefinitionDao.create(formDefinition));

        FormDefinition formDefinitionToUpdate = TestDataUtility.formDefWithTestValues();
        formDefinitionToUpdate.setName(null);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinitionToUpdate))
                .when()
                .put(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Form definition name cannot be null"));
    }

    /**
     * Verify that {@link FormDefinitionRestController#update} is working correctly
     * when an empty list of {@link edu.uwm.capstone.model.FieldDefinition}s is passed in.
     */
    @Test
    public void updatePreconditionFailedEmptyFieldDefinitions() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formsToCleanup.add(formDefinitionDao.create(formDefinition));

        FormDefinition formDefinitionToUpdate = TestDataUtility.formDefWithTestValues();
        formDefinitionToUpdate.setFieldDefs(Collections.emptyList());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinitionToUpdate))
                .when()
                .put(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Form definition must have at least one field definition"));
    }

    /**
     * Verify that {@link FormDefinitionRestController#update} is working correctly
     * when a field definition with an unknown id for a {@link FormDefinition} is passed in.
     */
    @Test
    public void updatePreconditionFailedUnknownFieldDefinitionId() throws Exception {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formsToCleanup.add(formDefinitionDao.create(formDefinition));

        FormDefinition formDefinitionToUpdate = TestDataUtility.formDefWithTestValues();
        Long randLong = TestDataUtility.randomLong();
        formDefinitionToUpdate.getFieldDefs().get(1).setId(randLong);

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(formDefinitionToUpdate))
                .when()
                .put(FormDefinitionRestController.FORM_DEF_PATH + formDefinition.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Could not update form definition " + formDefinition.getId() +
                " - found a field definition with id = " + randLong + " which is not associated with this form definition"));
    }

    /**
     * Verify that {@link FormDefinitionRestController #read} is working correctly
     * when a request for a {@link FormDefinition#id} is made.
     **/
    @Test
    public void readById() {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
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
     * Verify that {@link FormDefinitionRestController#readById} is working correctly
     * when a request for a non-existent {@link FormDefinition#id} is made.
     **/
    @Test
    public void readByIdNotFound() {
        Long formDefId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormDefinitionRestController.FORM_DEF_PATH + formDefId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Form definition with ID: " + formDefId + " not found."));
    }

    /**
     * Verify that {@link FormDefinitionRestController#readAll} is working correctly.
     */
    @Test
    public void readAll() {
        List<FormDefinition> persistedFormDefs = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
            formsToCleanup.add(formDefinition);
            persistedFormDefs.add(formDefinitionDao.create(formDefinition));
        }

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormDefinitionRestController.FORM_DEF_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        assertEquals(persistedFormDefs, response.body().jsonPath().getList(".", FormDefinition.class));
    }

    /**
     * Verify that {@link FormDefinitionRestController#deleteById} is working correctly
     * when a request for a {@link FormDefinition#id} is made.
     **/
    @Test
    public void deleteById() {
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
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
     * Verify that {@link FormDefinitionRestController#deleteById} is working correctly
     * when a request for a non-existent {@link FormDefinition#id} is made.
     **/
    @Test
    public void deleteByIdNotFound() {
        Long formDefId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(FormDefinitionRestController.FORM_DEF_PATH + formDefId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not delete form definition " + formDefId + " - record not found."));
    }
}
