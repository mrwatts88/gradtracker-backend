package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.helper.DefaultEntities;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.DegreeProgramState;
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
import java.util.Iterator;
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

    private ObjectMapper mapper = new ObjectMapper();

    private List<DegreeProgram> degreeProgramsToClean = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(dpDao);

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
        degreeProgramsToClean.forEach(degreeProg -> dpDao.delete(degreeProg.getId()));
        degreeProgramsToClean.clear();
    }

    /**
     * Verify that {@link DegreeProgramRestController#create} is working correctly.
     */
    @Test
    public void create() throws Exception {
        DegreeProgram createDegreeProgram = TestDataUtility.randomDegreeProgram(TestDataUtility.randomInt(1, 10));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(createDegreeProgram))
                .when()
                .post(DegreeProgramRestController.DEGREE_PROGRAM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);
        degreeProgramsToClean.add(receivedProgram);

        // verify dp was created
        DegreeProgram verifyProgram = dpDao.read(receivedProgram.getId());
        assertEquals(receivedProgram, verifyProgram);

        // check name and description
        assertEquals(createDegreeProgram.getName(), verifyProgram.getName());
        assertEquals(createDegreeProgram.getDescription(), verifyProgram.getDescription());

        // check degree program states
        assertEquals(createDegreeProgram.getDegreeProgramStates().size(), verifyProgram.getDegreeProgramStates().size());
        Iterator<DegreeProgramState> createDPIter = createDegreeProgram.iterator();
        Iterator<DegreeProgramState> verifyDPIter = verifyProgram.iterator();

        while (createDPIter.hasNext()) {
            DegreeProgramState cdps = createDPIter.next();
            DegreeProgramState vdps = verifyDPIter.next();

            assertEquals(cdps.getName(), vdps.getName());
            assertEquals(cdps.getDescription(), vdps.getDescription());
            assertEquals(cdps.isInitial(), vdps.isInitial());
        }
    }

    /**
     * Verify that {@link DegreeProgramRestController#readById} is working correctly.
     */
    @Test
    public void readById() {
        DegreeProgram createDegreeProgram = TestDataUtility.randomDegreeProgram(TestDataUtility.randomInt(1, 10));
        dpDao.create(createDegreeProgram);
        degreeProgramsToClean.add(createDegreeProgram);


        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(DegreeProgramRestController.DEGREE_PROGRAM_PATH + createDegreeProgram.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);

        assertNotNull(receivedProgram);
        assertNotNull(receivedProgram.getId());
        assertNotNull(receivedProgram.getCreatedDate());
        assertNotNull(receivedProgram.getDegreeProgramStates());

        assertEquals(createDegreeProgram.getName(), receivedProgram.getName());
        assertEquals(createDegreeProgram.getDescription(), receivedProgram.getDescription());
        assertNotNull(createDegreeProgram.getDegreeProgramStates());
        assertTrue(receivedProgram.getDegreeProgramStates().containsAll(createDegreeProgram.getDegreeProgramStates()));
        assertNotNull(createDegreeProgram.initialState());
    }

    /**
     * Verify that {@link DegreeProgramRestController#readAll} is working correctly.
     */
    @Test
    public void readAll() {
        List<DegreeProgram> persistedDegreePrograms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            DegreeProgram degreeProgram = TestDataUtility.randomDegreeProgram(TestDataUtility.randomInt(1, 10));
            dpDao.create(degreeProgram);
            degreeProgramsToClean.add(degreeProgram);
            persistedDegreePrograms.add(degreeProgram);
        }

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(DegreeProgramRestController.DEGREE_PROGRAM_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        assertEquals(persistedDegreePrograms, response.body().jsonPath().getList(".", DegreeProgram.class));
    }

    /**
     * Verify that {@link DegreeProgramRestController#update} is working correctly.
     */
    @Test
    public void updateById() throws Exception {
        DegreeProgram createDegreeProgram = TestDataUtility.randomDegreeProgram(TestDataUtility.randomInt(1, 10));
        dpDao.create(createDegreeProgram);
        degreeProgramsToClean.add(createDegreeProgram);

        DegreeProgram dpToUpdate = TestDataUtility.randomDegreeProgram(TestDataUtility.randomInt(1, 10));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(dpToUpdate))
                .when()
                .put(DegreeProgramRestController.DEGREE_PROGRAM_PATH + createDegreeProgram.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        DegreeProgram receivedProgram = response.body().as(DegreeProgram.class);

        // verify dp was updated
        DegreeProgram verifyProgram = dpDao.read(createDegreeProgram.getId());
        assertEquals(receivedProgram, verifyProgram);

        // check name and description
        assertEquals(dpToUpdate.getName(), verifyProgram.getName());
        assertEquals(dpToUpdate.getDescription(), verifyProgram.getDescription());

        // check degree program states
        assertEquals(dpToUpdate.getDegreeProgramStates().size(), verifyProgram.getDegreeProgramStates().size());
        Iterator<DegreeProgramState> updateDPIter = dpToUpdate.iterator();
        Iterator<DegreeProgramState> verifyDPIter = verifyProgram.iterator();

        while (updateDPIter.hasNext()) {
            DegreeProgramState udps = updateDPIter.next();
            DegreeProgramState vdps = verifyDPIter.next();

            assertEquals(udps.getName(), vdps.getName());
            assertEquals(udps.getDescription(), vdps.getDescription());
            assertEquals(udps.isInitial(), vdps.isInitial());
        }
    }

    /**
     * Verify that {@link DegreeProgramRestController#deleteById} is working correctly.
     */
    @Test
    public void deleteById() {
        DegreeProgram degreeProgram = TestDataUtility.randomDegreeProgram(5);
        dpDao.create(degreeProgram);

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .delete(DegreeProgramRestController.DEGREE_PROGRAM_PATH + degreeProgram.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();
    }

}

