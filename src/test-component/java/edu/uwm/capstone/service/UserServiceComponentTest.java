package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.User;
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

import static edu.uwm.capstone.security.SecurityConstants.DEFAULT_USER_EMAIL;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserServiceComponentTest {

    @Autowired
    private UserService userService;

    private List<User> usersToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(userService);
    }

    @After
    public void teardown() {
        usersToCleanup.forEach(user -> userService.delete(user.getId()));
        usersToCleanup.clear();
    }

    /**
     * Verify that {@link UserService#create} is working correctly.
     */
    @Test
    public void create() {
        User createUser = TestDataUtility.userWithTestValues();
        String passwordBefore = createUser.getPassword();
        userService.create(createUser);
        assertNotNull(createUser.getId());
        assertNotNull(createUser.getCreatedDate());
        assertNotEquals(createUser.getPassword(), passwordBefore);
        usersToCleanup.add(createUser);
    }

    /**
     * Verify that {@link UserService#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullUser() {
        userService.create(null);
    }

    /**
     * Verify that {@link UserService#create} is working correctly when a request for a {@link User} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullUserId() {
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        userService.create(createUser);
    }

    /**
     * Verify that {@link UserService#create} is working correctly when a request for a {@link User} with a null email is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullUserEmail() {
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setEmail(null);
        userService.create(createUser);
    }

    /**
     * Verify that {@link UserService#create} is working correctly when a request for a {@link User} with a null password is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullUserPassword() {
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setPassword(null);
        userService.create(createUser);
    }

    /**
     * Verify that {@link UserService#create} is working correctly when a request for a {@link User} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createUserColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        User createUser = TestDataUtility.userWithTestValues();
        createUser.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        userService.create(createUser);
    }

    /**
     * Verify that {@link UserService#read} is working correctly.
     */
    @Test
    public void read() {
        User createUser = TestDataUtility.userWithTestValues();
        userService.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User readUser = userService.read(createUser.getId());
        assertNotNull(readUser);
        assertEquals(createUser.getId(), readUser.getId());
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserService#read} is working correctly.
     */
    @Test
    public void readByEmail() {
        User createUser = TestDataUtility.userWithTestValues();
        userService.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User readUser = userService.readByEmail(createUser.getEmail());
        assertNotNull(readUser);
        assertEquals(createUser.getId(), readUser.getId());
        assertEquals(createUser, readUser);
    }

    @Test
    public void readAll() {
        List<User> persistedUsers = new ArrayList<>();
        persistedUsers.add(userService.readByEmail(DEFAULT_USER_EMAIL)); // need default user in here
        int randInt = TestDataUtility.randomInt(10, 30);
        for(int i = 0; i < randInt; i++) {
            User user = TestDataUtility.userWithTestValues();
            userService.create(user);
            usersToCleanup.add(user);
            persistedUsers.add(user);
        }

        assertEquals(persistedUsers, userService.readAll());
    }

    /**
     * Verify that {@link UserService#read} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentUser() {
        // create a random user id that will not be in our local database
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        User user = userService.read(id);
        assertNull(user);
    }

    /**
     * Verify that {@link UserService#update} is working correctly.
     */
    @Test
    public void update() {
        User createUser = TestDataUtility.userWithTestValues();
        userService.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User verifyCreateUser = userService.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser, verifyCreateUser);

        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(createUser.getId());
        userService.update(updateUser);

        User verifyUpdateUser = userService.read(updateUser.getId());
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
     * Verify that {@link UserService#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullUser() {
        userService.update(null);
    }

    /**
     * Verify that {@link UserService#update} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentUser() {
        // create a random user id that will not be in our local database
        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        userService.update(updateUser);
    }

    /**
     * Verify that {@link UserService#update} is working correctly when a request for a null {@link User#email} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullUserEmail() {
        // create a random user id that will not be in our local database
        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setEmail(null);
        userService.update(updateUser);
    }

    /**
     * Verify that {@link UserService#update} is working correctly when a request for a null {@link User#password} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullUserPassword() {
        // create a random user id that will not be in our local database
        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setPassword(null);
        userService.update(updateUser);
    }

    /**
     * Verify that {@link UserService#update} is working correctly when a request for a {@link User} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateUserColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        User createUser = TestDataUtility.userWithTestValues();
        userService.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User verifyCreateUser = userService.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser.getId(), verifyCreateUser.getId());
        assertEquals(createUser, verifyCreateUser);

        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(createUser.getId());
        updateUser.setFirstName(RandomStringUtils.randomAlphabetic(2000));
        userService.update(updateUser);
    }

    /**
     * Verify that {@link UserService#delete} is working correctly.
     */
    @Test
    public void delete() {
        User createUser = TestDataUtility.userWithTestValues();
        userService.create(createUser);
        assertNotNull(createUser.getId());

        User verifyCreateUser = userService.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser.getId(), verifyCreateUser.getId());
        assertEquals(createUser, verifyCreateUser);

        userService.delete(createUser.getId());
    }

    /**
     * Verify that {@link UserService#delete} is working correctly when a request for a non-existent {@link User#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentUser() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        userService.delete(id);
    }
}
