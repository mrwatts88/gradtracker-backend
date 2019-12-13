package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.*;
import edu.uwm.capstone.helper.DefaultEntities;
import edu.uwm.capstone.model.*;
import edu.uwm.capstone.service.DegreeProgramService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static edu.uwm.capstone.security.SecurityConstants.AUTHENTICATE_URL;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link DegreeProgramRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DegreeProgramControllerComponentTest {
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
    private DegreeProgramDao dpDao;

    @Autowired
    private DegreeProgramStateDao stateDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<DegreeProgram> degreeProgramsToClean = new ArrayList<>();

    private List<DegreeProgramState> defaultState = new ArrayList<>();

    private List<User> usersToCleanup = new ArrayList<>();


    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(dpDao);

        RestAssured.port = port;
        RestAssured.basePath = basePath;
        defaultState.add(new DegreeProgramState(null, "Test state", "Testing", true));

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
        degreeProgramsToClean.forEach(degreeProg -> dpDao.delete(degreeProg.getId()));

        degreeProgramsToClean.clear();
        defaultState.clear();
        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
    }

    /**
     * Verify that {@link DegreeProgramRestController#create} is working correctly.
     */
    @Test
    public void create() throws Exception {
        DegreeProgram testProg = new DegreeProgram("Program for test!", "Testing data", defaultState);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(testProg))
                .when()
                .post(DegreeProgramRestController.DEGREE_PROGRAM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedProgram);
        assertNotNull(receivedProgram.getId());
        assertNotNull(receivedProgram.getCreatedDate());
        assertEquals(dpDao.read(receivedProgram.getId()), receivedProgram);
        assertEquals(dpDao.read(receivedProgram.getId()).getName(), receivedProgram.getName());
        assertEquals(dpDao.read(receivedProgram.getId()).getDescription(), receivedProgram.getDescription());
        List<DegreeProgramState> states = receivedProgram.getDegreeProgramStates();
        assertNotNull(states);
        boolean initialStateFound = false;
        for (DegreeProgramState ds: states){
            if (ds.isInitial()){
                initialStateFound = true;
                break;
            }
        }
        assertTrue(initialStateFound);
        degreeProgramsToClean.add(receivedProgram);
    }

    /**
     * Verify that {@link DegreeProgramRestController#readById} is working correctly.
     */
    @Test
    public void readById() throws Exception {
        DegreeProgram testProg = new DegreeProgram("Program for test!", "Testing data", defaultState);
        dpDao.create(testProg);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(testProg))
                .when()
                .get(DegreeProgramRestController.DEGREE_PROGRAM_PATH+testProg.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedProgram);
        assertNotNull(receivedProgram.getId());
        assertNotNull(receivedProgram.getCreatedDate());
        assertEquals(dpDao.read(receivedProgram.getId()).getId(), testProg.getId());
      assertEquals(dpDao.read(receivedProgram.getId()).getName(), testProg.getName());
        assertEquals(dpDao.read(receivedProgram.getId()).getDescription(), testProg.getDescription());
        List<DegreeProgramState> states = receivedProgram.getDegreeProgramStates();
        assertNotNull(states);
        boolean initialStateFound = false;
        for (DegreeProgramState ds: states){
            if (ds.isInitial()){
                initialStateFound = true;
                break;
            }
        }
        assertTrue(initialStateFound);
        degreeProgramsToClean.add(receivedProgram);
    }

    /**
     * Verify that {@link DegreeProgramRestController#readAll} is working correctly.
     */
    @Test
    public void readAll() throws Exception {
        List<DegreeProgram> persistedDegreePrograms = new ArrayList<>();
        DegreeProgram testProg = new DegreeProgram("Program for test!", "Testing data", defaultState);
        dpDao.create(testProg);

        List<DegreeProgramState> nextStates = new ArrayList<>();
        nextStates.add(new DegreeProgramState(null, "Another state!", "Testing more!", true));

        DegreeProgram testProg2 = new DegreeProgram("Next Program!", "Another Test!", nextStates);
        dpDao.create(testProg2);

        List<DegreeProgramState> lastStates = new ArrayList<>();
        lastStates.add(new DegreeProgramState(null, "Last state!", "Testing final!", true));

        DegreeProgram testProg3 = new DegreeProgram("Final Program!", "Testing final!", lastStates);
        dpDao.create(testProg3);

        persistedDegreePrograms.add(testProg);
        persistedDegreePrograms.add(testProg2);
        persistedDegreePrograms.add(testProg3);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(DegreeProgramRestController.DEGREE_PROGRAM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        List<DegreeProgram> returnedList = response.body().jsonPath().getList(".", DegreeProgram.class);
        boolean allContained = true;

        for (DegreeProgram ds: returnedList) {
            if (!persistedDegreePrograms.contains(ds)) allContained = false;
            //degreeProgramsToClean.add(ds); Call doesn't work here for some reason?
        }

        assertTrue(allContained);
    }

    /**
     * Verify that {@link DegreeProgramRestController#update} is working correctly.
     */
    @Test
    public void updateById() throws Exception {
        DegreeProgram testProg = new DegreeProgram("Program for test!", "Testing data", defaultState);
        dpDao.create(testProg);

        DegreeProgram dpToUpdate = TestDataUtility.randomDegreeProgram(1);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(dpToUpdate))
                .when()
                .put(DegreeProgramRestController.DEGREE_PROGRAM_PATH+testProg.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedProgram);
        assertNotNull(receivedProgram.getId());
        assertNotNull(receivedProgram.getCreatedDate());
        assertNotEquals(testProg, receivedProgram);
        List<DegreeProgramState> states = receivedProgram.getDegreeProgramStates();
        assertNotNull(states);
        boolean initialStateFound = false;
        for (DegreeProgramState ds: states){
            if (ds.isInitial()){
                initialStateFound = true;
                break;
            }
        }
        assertTrue(initialStateFound);
        degreeProgramsToClean.add(receivedProgram);
    }

    /**
     * Verify that {@link DegreeProgramRestController#deleteById} is working correctly.
     */
    @Test
    public void deleteById() throws Exception {
        DegreeProgram testProg = new DegreeProgram("Program for test!", "Testing data", defaultState);
        dpDao.create(testProg);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(testProg))
                .when()
                .delete(DegreeProgramRestController.DEGREE_PROGRAM_PATH+testProg.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

}

