package edu.uwm.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
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
import static edu.uwm.capstone.util.TestDataUtility.profileWithTestValues;
import static edu.uwm.capstone.util.TestDataUtility.randomLong;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that
 * the REST endpoints provided by the {@link ProfileRestController} are working correctly.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProfileRestControllerComponentTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProfileDao profileDao;

    private ObjectMapper mapper = new ObjectMapper();

    private List<Profile> profilesToCleanup = new ArrayList<>();

    private String authorizationToken;

    @Before
    public void setUp() throws Exception {
        assertNotNull(basePath);
        assertNotNull(restTemplate);
        assertNotNull(profileDao);

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
        profilesToCleanup.forEach(profile -> profileDao.delete(profile.getId()));
        profilesToCleanup.clear();
    }

    @Test
    public void create() throws Exception {
        Profile profileToCreate = profileWithTestValues();

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(profileToCreate))
                .when()
                .post(ProfileRestController.PROFILE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Profile receivedProfile = response.body().as(Profile.class);

        //if Id is populated - record created successfully
        assertNotNull(receivedProfile.getId());
        profilesToCleanup.add(receivedProfile);

        assertNotNull(receivedProfile.getCreatedDate());
        assertEquals(profileDao.read(receivedProfile.getId()), receivedProfile);
    }

    @Test
    public void createPreconditionFailed() throws Exception {
        Profile profile = profileWithTestValues();
        profile.setId(randomLong());

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(mapper.writeValueAsString(profile))
                .when()
                .post(ProfileRestController.PROFILE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value()).body("message", equalTo("Profile ID must be null"));
    }

    @Test
    public void update() throws Exception {
        Profile profile = profileWithTestValues();
        profilesToCleanup.add(profileDao.create(profile));

        Profile profileToUpdate = profileWithTestValues();
        profileToUpdate.setId(profile.getId());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(profileToUpdate))
                .when()
                .put(ProfileRestController.PROFILE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Profile verifyProfile = profileDao.read(profile.getId());
        assertNotNull(verifyProfile.getUpdatedDate());
        assertEquals(profileToUpdate.getId(), verifyProfile.getId());
    }


    @Test
    public void updatePreconditionFailed() throws Exception {
        Profile profileToUpdate = profileWithTestValues();
        profileToUpdate.setId(randomLong());

        // exercise endpoint
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(new Header("Authorization", authorizationToken))
                .body(mapper.writeValueAsString(profileToUpdate))
                .when()
                .put(ProfileRestController.PROFILE_PATH)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Could not update Profile " + profileToUpdate.getId() + " - record not found."));
    }

    @Test
    public void readById() {
        Profile profile = profileWithTestValues();
        profilesToCleanup.add(profileDao.create(profile));

        // exercise endpoint
        ExtractableResponse<Response> response = given()
                .header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(ProfileRestController.PROFILE_PATH + profile.getId())
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value()).extract();

        Profile receivedProfile = response.body().as(Profile.class);
        assertEquals(profile, receivedProfile);
    }

    @Test
    public void readByIdNotFound() {
        Long profileId = randomLong();

        // exercise endpoint
        given().header(new Header("Authorization", authorizationToken))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(ProfileRestController.PROFILE_PATH + profileId)
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value()).body("message", equalTo("Profile with ID: " + profileId + " not found."));
    }
}
