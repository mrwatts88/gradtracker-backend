package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProfileServiceComponentTest {

    @Autowired
    private ProfileService profileService;

    private List<Profile> profilesToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(profileService);
    }

    @After
    public void teardown() {
        profilesToCleanup.forEach(profile -> profileService.delete(profile.getId()));
        profilesToCleanup.clear();
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly.
     */
    @Test
    public void create() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        String passwordBefore = createProfile.getPassword();
        profileService.create(createProfile);
        assertNotNull(createProfile.getId());
        assertNotNull(createProfile.getCreatedDate());
        assertNotEquals(createProfile.getPassword(), passwordBefore);
        profilesToCleanup.add(createProfile);
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullProfile() {
        profileService.create(null);
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly when a request for a {@link Profile} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullProfileId() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        profileService.create(createProfile);
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly when a request for a {@link Profile} with a null email is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullProfileEmail() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setEmail(null);
        profileService.create(createProfile);
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly when a request for a {@link Profile} with a null password is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullProfilePassword() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setPassword(null);
        profileService.create(createProfile);
    }

    /**
     * Verify that {@link ProfileService#create} is working correctly when a request for a {@link Profile} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createProfileColumnTooLong() {
        // generate a test profile value with a column that will exceed the database configuration
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        profileService.create(createProfile);
    }

    /**
     * Verify that {@link ProfileService#read} is working correctly.
     */
    @Test
    public void read() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileService.create(createProfile);
        assertNotNull(createProfile.getId());
        profilesToCleanup.add(createProfile);

        Profile readProfile = profileService.read(createProfile.getId());
        assertNotNull(readProfile);
        assertEquals(createProfile.getId(), readProfile.getId());
        assertEquals(createProfile, readProfile);
    }

    /**
     * Verify that {@link ProfileService#read} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentProfile() {
        // create a random profile id that will not be in our local database
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        Profile profile = profileService.read(id);
        assertNull(profile);
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly.
     */
    @Test
    public void update() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileService.create(createProfile);
        assertNotNull(createProfile.getId());
        profilesToCleanup.add(createProfile);

        Profile verifyCreateProfile = profileService.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile, verifyCreateProfile);

        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(createProfile.getId());
        profileService.update(updateProfile);

        Profile verifyUpdateProfile = profileService.read(updateProfile.getId());
        assertNotNull(verifyUpdateProfile);
        assertEquals(createProfile.getId(), verifyUpdateProfile.getId());
        assertEquals(updateProfile.getFirstName(), verifyUpdateProfile.getFirstName());
        assertEquals(updateProfile.getLastName(), verifyUpdateProfile.getLastName());
        assertEquals(updateProfile.getPantherId(), verifyUpdateProfile.getPantherId());
        assertEquals(updateProfile.getEmail(), verifyUpdateProfile.getEmail());

        assertNotEquals(verifyCreateProfile.getFirstName(), verifyUpdateProfile.getFirstName());
        assertNotEquals(verifyCreateProfile.getLastName(), verifyUpdateProfile.getLastName());
        assertNotEquals(verifyCreateProfile.getPantherId(), verifyUpdateProfile.getPantherId());
        assertNotEquals(verifyCreateProfile.getEmail(), verifyUpdateProfile.getEmail());
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullProfile() {
        profileService.update(null);
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentProfile() {
        // create a random profile id that will not be in our local database
        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        profileService.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly when a request for a null {@link Profile#email} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullProfileEmail() {
        // create a random profile id that will not be in our local database
        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setEmail(null);
        profileService.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly when a request for a null {@link Profile#password} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullProfilePassword() {
        // create a random profile id that will not be in our local database
        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setPassword(null);
        profileService.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileService#update} is working correctly when a request for a {@link Profile} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateProfileColumnTooLong() {
        // generate a test profile value with a column that will exceed the database configuration
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileService.create(createProfile);
        assertNotNull(createProfile.getId());
        profilesToCleanup.add(createProfile);

        Profile verifyCreateProfile = profileService.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile.getId(), verifyCreateProfile.getId());
        assertEquals(createProfile, verifyCreateProfile);

        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(createProfile.getId());
        updateProfile.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        profileService.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileService#delete} is working correctly.
     */
    @Test
    public void delete() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileService.create(createProfile);
        assertNotNull(createProfile.getId());

        Profile verifyCreateProfile = profileService.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile.getId(), verifyCreateProfile.getId());
        assertEquals(createProfile, verifyCreateProfile);

        profileService.delete(createProfile.getId());
    }

    /**
     * Verify that {@link ProfileService#delete} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentProfile() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        profileService.delete(id);
    }
}
