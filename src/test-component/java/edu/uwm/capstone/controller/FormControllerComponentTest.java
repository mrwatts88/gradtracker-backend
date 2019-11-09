package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.FormDao;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.model.FormDefinition;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.uwm.capstone.security.SecurityConstants.AUTHENTICATE_URL;
import static edu.uwm.capstone.security.SecurityConstants.DEFAULT_USER_CREDENTIALS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link FormRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FormControllerComponentTest {
    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FormDao formDao;

    @Autowired
    private FormDefinitionDao formDefinitionDao;

    @Autowired
    private UserDao userDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<FormDefinition> formDefsToCleanup = new ArrayList<>();

    private List<Form> formsToCleanup = new ArrayList<>();

    private List<User> usersToCleanup = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(formDao);

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
        formsToCleanup.forEach(form -> formDao.delete(form.getId()));
        formsToCleanup.clear();

        formDefsToCleanup.forEach(formDef -> formDefinitionDao.delete(formDef.getId()));
        formDefsToCleanup.clear();

        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
    }

    /**
     * Verify that {@link FormRestController#create} is working correctly.
     */
    @Test
    public void create() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createForm))
                .when()
                .post(FormRestController.FORM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Form receivedForm = response.body().as(Form.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedForm);
        assertNotNull(receivedForm.getId());
        assertNotNull(receivedForm.getCreatedDate());
        assertEquals(formDao.read(receivedForm.getId()), receivedForm);
        formsToCleanup.add(receivedForm);
    }

    /**
     * Verify that {@link FormRestController#create} is working correctly when an incorrect id is passed in.
     */
    @Test
    public void createPreconditionFailedId() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setId(TestDataUtility.randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createForm))
                .when()
                .post(FormRestController.FORM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .body("message", equalTo("Form id should be null"));
    }

    /**
     * Verify that {@link FormRestController#create} is working correctly
     * when an empty list of field definitions is passed in.
     */
    @Test
    public void createPreconditionFailedEmptyFieldDefinitions() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setFields(Collections.emptyList());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createForm))
                .when()
                .post(FormRestController.FORM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .body("message", equalTo("Form should have same number of fields as its form definition"));
    }

    /**
     * Verify that {@link FormRestController#update} is working correctly.
     */
    @Test
    public void update() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        formsToCleanup.add(createForm);

        // get new "updated" form with the same form definition and user id
        Form formToUpdate = TestDataUtility.formWithTestValues(createFormDef, user.getId());

        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(formToUpdate.getFields()))
                .when()
                .put(FormRestController.FORM_PATH + createForm.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Form verifyForm = formDao.read(createForm.getId());
        assertNotNull(verifyForm.getUpdatedDate());
        assertNotEquals(createForm, verifyForm);
    }

    /**
     * Verify that {@link FormRestController#create} is working correctly when an incorrect id is passed in.
     */
    @Test
    public void updateNotFound() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        Long randomFormId = TestDataUtility.randomLong();

        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(createForm.getFields()))
                .when()
                .put(FormRestController.FORM_PATH + randomFormId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not update form " + randomFormId + " - record not found."));
    }

    /**
     * Verify that {@link FormRestController#update} is working correctly
     * when an empty list of {@link Field} is passed in.
     */
    @Test
    public void updatePreconditionFailedEmptyField() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        formsToCleanup.add(createForm);

        createForm.setCreatedDate(null);
        createForm.setFields(Collections.emptyList());

        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createForm.getFields()))
                .when()
                .put(FormRestController.FORM_PATH + createForm.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Form should have same number of fields as its form definition"));
    }

    /**
     * Verify that {@link FormRestController#update} is working correctly
     * when a field with an unknown id for a {@link Form} is passed in.
     */
    @Test
    public void updatePreconditionFailedUnknownFieldDefinitionId() throws Exception {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        formsToCleanup.add(createForm);

        Long randLong = TestDataUtility.randomLong();
        createForm.getFields().get(1).setId(randLong);
        createForm.setCreatedDate(null);
        createForm.getFields().forEach(field -> field.setCreatedDate(null));

        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createForm.getFields()))
                .when()
                .put(FormRestController.FORM_PATH + createForm.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Could not update form " + createForm.getId() +
                " - found a field with id = " + randLong + " which is not associated with this form"));
    }

    /**
     * Verify that {@link FormRestController #read} is working correctly
     * when a request for a {@link Form#id} is made.
     **/
    @Test
    public void readById() {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        formsToCleanup.add(formDao.create(createForm));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormRestController.FORM_PATH + createForm.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Form receivedForm = response.body().as(Form.class);
        assertEquals(createForm, receivedForm);
    }

    /**
     * Verify that {@link FormRestController#readById} is working correctly
     * when a request for a non-existent {@link FormDefinition#id} is made.
     **/
    @Test
    public void readByIdNotFound() {
        Long formId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormRestController.FORM_PATH + formId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Form with ID: " + formId + " not found."));
    }

    /**
     * Verify that {@link FormRestController#readAll} is working correctly.
     */
    @Test
    public void readAll() {
        List<Form> persistedForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            // need a user in the db connected to the form
            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));

            formsToCleanup.add(createForm);
            persistedForms.add(createForm);
        }

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormRestController.FORM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        assertEquals(persistedForms, response.body().jsonPath().getList(".", Form.class));
    }

    /**
     * Verify that {@link FormRestController#readAll} is working correctly.
     */
    @Test
    public void readAllByFormDefId() {
        List<Form> forms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        Long formDefId = createFormDef.getId();

        // create user's forms
        for (int i = 0; i < randInt; i++) {
            // need a user in the db connected to the form
            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
            formsToCleanup.add(createForm);
            forms.add(createForm);
        }

        // create more forms
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            // need a user in the db connected to the form
            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            formsToCleanup.add(formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormRestController.FORM_PATH + "formDef/" + formDefId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        assertEquals(forms, response.body().jsonPath().getList(".", Form.class));
    }

    /**
     * Verify that {@link FormRestController#readAll} is working correctly.
     */
    @Test
    public void readAllByUserId() {
        List<Form> userForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);
        Long userId = user.getId();

        // create user's forms
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
            formsToCleanup.add(createForm);
            userForms.add(createForm);
        }

        // create more forms
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            // need a user in the db connected to the form
            user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            formsToCleanup.add(formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(FormRestController.FORM_PATH + "user/" + userId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        assertEquals(userForms, response.body().jsonPath().getList(".", Form.class));
    }

    /**
     * Verify that {@link FormRestController#deleteById} is working correctly
     * when a request for a {@link Form#id} is made.
     **/
    @Test
    public void deleteById() {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());

        formDao.create(createForm);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(FormRestController.FORM_PATH + createForm.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    /**
     * Verify that {@link FormRestController#deleteById} is working correctly
     * when a request for a non-existent {@link Form#id} is made.
     **/
    @Test
    public void deleteByIdNotFound() {
        Long formId = TestDataUtility.randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(FormRestController.FORM_PATH + formId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not delete form " + formId + " - record not found."));
    }
}
