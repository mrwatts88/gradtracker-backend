package edu.uwm.capstone.db;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.profile.Profile;
import edu.uwm.capstone.util.TestDataUtility;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class ProfileDaoUnitTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ProfileDao profileDao;

    @Before
    public void setUp() {
        assertNotNull(profileDao);
        assertNotNull(profileDao.sql("createProfile"));
        assertNotNull(profileDao.sql("readProfile"));
        assertNotNull(profileDao.sql("updateProfile"));
        assertNotNull(profileDao.sql("deleteProfile"));
    }

    /**
     * Verify that {@link ProfileDao#create} is working correctly.
     */
    @Test
    public void create() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileDao.create(createProfile);
        assertNotNull(createProfile.getId());
    }

    /**
     * Verify that {@link ProfileDao#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullProfile() {
        profileDao.create(null);
    }

    /**
     * Verify that {@link ProfileDao#create} is working correctly when a request for a {@link Profile} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullProfileId() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        profileDao.create(createProfile);
    }

    /**
     * Verify that {@link ProfileDao#create} is working correctly when a request for a {@link Profile} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createProfileColumnTooLong() {
        // generate a test profile value with a column that will exceed the database configuration
        Profile createProfile = TestDataUtility.profileWithTestValues();
        createProfile.setName(RandomStringUtils.randomAlphabetic(2000));
        profileDao.create(createProfile);
    }

    /**
     * Verify that {@link ProfileDao#read} is working correctly.
     */
    @Test
    public void read() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileDao.create(createProfile);
        assertNotNull(createProfile.getId());

        Profile readProfile = profileDao.read(createProfile.getId());
        assertNotNull(readProfile);
        assertEquals(createProfile.getId(), readProfile.getId());
        assertEquals(createProfile, readProfile);
    }

    /**
     * Verify that {@link ProfileDao#read} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test
    public void readNonExistentProfile() {
        // create a random profile id that will not be in our local database
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        Profile profile = profileDao.read(id);
        assertNull(profile);
    }

    /**
     * Verify that {@link ProfileDao#update} is working correctly.
     */
    @Test
    public void update() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileDao.create(createProfile);
        assertNotNull(createProfile.getId());

        Profile verifyCreateProfile = profileDao.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile.getId(), verifyCreateProfile.getId());
        assertEquals(createProfile, verifyCreateProfile);

        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(createProfile.getId());
        profileDao.update(updateProfile);

        Profile verifyUpdateProfile = profileDao.read(updateProfile.getId());
        assertNotNull(verifyUpdateProfile);
        assertEquals(createProfile.getId(), verifyUpdateProfile.getId());
        assertEquals(updateProfile.getName(), verifyUpdateProfile.getName());
    }

    /**
     * Verify that {@link ProfileDao#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullProfile() {
        profileDao.update(null);
    }

    /**
     * Verify that {@link ProfileDao#update} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentProfile() {
        // create a random profile id that will not be in our local database
        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        profileDao.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileDao#update} is working correctly when a request for a {@link Profile} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateProfileColumnTooLong() {
        // generate a test profile value with a column that will exceed the database configuration
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileDao.create(createProfile);
        assertNotNull(createProfile.getId());

        Profile verifyCreateProfile = profileDao.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile.getId(), verifyCreateProfile.getId());
        assertEquals(createProfile, verifyCreateProfile);

        Profile updateProfile = TestDataUtility.profileWithTestValues();
        updateProfile.setId(createProfile.getId());
        updateProfile.setName(RandomStringUtils.randomAlphabetic(2000));
        profileDao.update(updateProfile);
    }

    /**
     * Verify that {@link ProfileDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        Profile createProfile = TestDataUtility.profileWithTestValues();
        profileDao.create(createProfile);
        assertNotNull(createProfile.getId());

        Profile verifyCreateProfile = profileDao.read(createProfile.getId());
        assertNotNull(verifyCreateProfile);
        assertEquals(createProfile.getId(), verifyCreateProfile.getId());
        assertEquals(createProfile, verifyCreateProfile);

        profileDao.delete(createProfile.getId());

        Profile verifyDeleteProfile = profileDao.read(createProfile.getId());
        assertNull(verifyDeleteProfile);
    }

    /**
     * Verify that {@link ProfileDao#delete} is working correctly when a request for a non-existent {@link Profile#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentProfile() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        profileDao.delete(id);
    }

}
