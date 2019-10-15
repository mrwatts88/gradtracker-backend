package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class UserDaoComponentTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserDao userDao;

    @Before
    public void setUp() {
        assertNotNull(userDao);
        assertNotNull(userDao.sql("createUser"));
        assertNotNull(userDao.sql("readUser"));
        assertNotNull(userDao.sql("updateUser"));
        assertNotNull(userDao.sql("deleteUser"));
        assertNotNull(userDao.sql("readUserByEmail"));
        assertNotNull(userDao.sql("readAllUsers"));
    }

    /**
     * Verify that {@link UserDao#create} is working correctly.
     */
    @Test
    public void create() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());
    }

    /**
     * Verify that {@link UserDao#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullUser() {
        userDao.create(null);
    }

    /**
     * Verify that {@link UserDao#create} is working correctly when a request for a {@link User} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullUserId() {
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        userDao.create(createUser);
    }

    /**
     * Verify that {@link UserDao#create} is working correctly when a request for a {@link User} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createUserColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        userDao.create(createUser);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly.
     */
    @Test
    public void read() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User readUser = userDao.read(createUser.getId());
        assertNotNull(readUser);
        assertEquals(createUser.getId(), readUser.getId());
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test
    public void readNonExistentUser() {
        // create a random user id that will not be in our local database
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        User user = userDao.read(id);
        assertNull(user);
    }

    /**
     * Verify that {@link UserDao#readByEmail(String)} is working correctly.
     */
    @Test
    public void readByEmail() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User readUser = userDao.readByEmail(createUser.getEmail());
        assertNotNull(readUser);
        assertEquals(createUser.getId(), readUser.getId());
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly when a request for a non-existent {@link User#email} is made.
     */
    @Test
    public void readNonExistentUserByEmail() {
        // create a random email that will not be in our local database
        String email = new Random().longs(10000L, Long.MAX_VALUE).findAny().toString();
        User user = userDao.readByEmail(email);
        assertNull(user);
    }

    /**
     * Verify that {@link UserDao#update} is working correctly.
     */
    @Test
    public void update() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User verifyCreateUser = userDao.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser.getId(), verifyCreateUser.getId());
        assertEquals(createUser.getFirstName(), verifyCreateUser.getFirstName());
        assertEquals(createUser.getLastName(), verifyCreateUser.getLastName());
        assertEquals(createUser.getPantherId(), verifyCreateUser.getPantherId());
        assertEquals(createUser.getEmail(), verifyCreateUser.getEmail());

        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(createUser.getId());
        userDao.update(updateUser);

        User verifyUpdateUser = userDao.read(updateUser.getId());
        assertNotNull(verifyUpdateUser);
        assertEquals(createUser.getId(), verifyUpdateUser.getId());
        assertEquals(updateUser.getFirstName(), verifyUpdateUser.getFirstName());
        assertEquals(updateUser.getLastName(), verifyUpdateUser.getLastName());
        assertEquals(updateUser.getPantherId(), verifyUpdateUser.getPantherId());
        assertEquals(updateUser.getEmail(), verifyUpdateUser.getEmail());

        assertNotEquals(verifyCreateUser.getFirstName(), verifyUpdateUser.getFirstName());
        assertNotEquals(verifyCreateUser.getLastName(), verifyUpdateUser.getLastName());
        assertNotEquals(verifyCreateUser.getPantherId(), verifyUpdateUser.getPantherId());
        assertNotEquals(verifyCreateUser.getEmail(), verifyUpdateUser.getEmail());

    }

    /**
     * Verify that {@link UserDao#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullUser() {
        userDao.update(null);
    }

    /**
     * Verify that {@link UserDao#update} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentUser() {
        // create a random user id that will not be in our local database
        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        userDao.update(updateUser);
    }

    /**
     * Verify that {@link UserDao#update} is working correctly when a request for a {@link User} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateUserColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User verifyCreateUser = userDao.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser.getId(), verifyCreateUser.getId());
        assertEquals(createUser, verifyCreateUser);

        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(createUser.getId());
        updateUser.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        userDao.update(updateUser);
    }

    /**
     * Verify that {@link UserDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User verifyCreateUser = userDao.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser.getId(), verifyCreateUser.getId());
        assertEquals(createUser, verifyCreateUser);

        userDao.delete(createUser.getId());

        User verifyDeleteUser = userDao.read(createUser.getId());
        assertNull(verifyDeleteUser);
    }

    /**
     * Verify that {@link UserDao#delete} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentUser() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        userDao.delete(id);
    }

    /**
     *
     */
    @Test
    public void readAllUser(){
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(userDao.readAll());
    }

}
