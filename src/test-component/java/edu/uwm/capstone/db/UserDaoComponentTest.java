package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.util.TestDataUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class UserDaoComponentTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserDao userDao;

    @Autowired
    DegreeProgramDao degreeProgramDao;

    private List<User> usersToCleanup = new ArrayList<>();
    private List<DegreeProgram> degreeProgramsToCleanup = new ArrayList<>();

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

    @After
    public void teardown() {
        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
        degreeProgramsToCleanup.forEach(dp -> degreeProgramDao.delete(dp.getId()));
        degreeProgramsToCleanup.clear();
    }

    /**
     * Verify that {@link UserDao#create} is working correctly.
     */
    @Test
    public void create() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);
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
        createUser.setId(TestDataUtility.randomLong());
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
        createUser.setFirstName(TestDataUtility.randomAlphabetic(2000));
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
        usersToCleanup.add(createUser);

        User readUser = userDao.read(createUser.getId());
        assertNotNull(readUser);
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserDao#readByEmail} is working correctly.
     */
    @Test
    public void readByEmail() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User readUser = userDao.readByEmail(createUser.getEmail());
        assertNotNull(readUser);
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserDao#readByPantherId} is working correctly.
     */
    @Test
    public void readByPantherId() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        User readUser = userDao.readByPantherId(createUser.getPantherId());
        assertNotNull(readUser);
        assertEquals(createUser, readUser);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly when a request for a non-existent {@link User #id} is made.
     */
    @Test
    public void readNonExistentUserById() {
        // create a random user id that will not be in our local database
        Long id = TestDataUtility.randomLong();
        User user = userDao.read(id);
        assertNull(user);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly when a request for a non-existent {@link User #email} is made.
     */
    @Test
    public void readNonExistentUserByEmail() {
        // create a random email that will not be in our local database
        String email = TestDataUtility.randomAlphanumeric(20);
        User user = userDao.readByEmail(email);
        assertNull(user);
    }

    /**
     * Verify that {@link UserDao#read} is working correctly when a request for a non-existent {@link User #email} is made.
     */
    @Test
    public void readNonExistentUserByPantherId() {
        String pantherId = TestDataUtility.randomAlphanumeric(20);
        User user = userDao.readByPantherId(pantherId);
        assertNull(user);
    }

    /**
     * Verify that {@link UserDao#readAll} is working correctly
     */
    @Test
    public void readAllUsers() {
        List<User> persistedUsers = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            User user = TestDataUtility.userWithTestValues();
            userDao.create(user);
            persistedUsers.add(user);
            usersToCleanup.add(user);
        }
        assertEquals(persistedUsers, userDao.readAll());
    }

    /**
     * Verify that {@link UserDao#update} is working correctly.
     */
    @Test
    public void update() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

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
     * Verify that {@link UserDao#update} is working correctly when a request for a non-existent {@link User #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentUser() {
        // create a random user id that will not be in our local database
        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(TestDataUtility.randomLong());
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
        usersToCleanup.add(createUser);

        userDao.create(createUser);
        assertNotNull(createUser.getId());

        User updateUser = TestDataUtility.userWithTestValues();
        updateUser.setId(createUser.getId());
        updateUser.setFirstName(TestDataUtility.randomAlphabetic(2000));
        userDao.update(updateUser);
    }

    /**
     * Verify that {@link UserDao#updateState} is working correctly.
     */
    @Test
    public void updateState() {
        User createUser = TestDataUtility.userWithTestValues();
        // Create an User
        userDao.create(createUser);

        // Verify user Id got generated
        assertNotNull(createUser.getId());
        usersToCleanup.add(createUser);

        // Create a DegreeProgram
        DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(5);
        degreeProgramDao.create(dp);

        // Verify dp Id got generated
        assertNotNull(dp.getId());
        degreeProgramsToCleanup.add(dp);

        // Generate new User object with same Id as createUser object
        // and new DegreeProgramState => CurrentStateId
        User updateUser = new User();
        updateUser.setId(createUser.getId());
        updateUser.setCurrentState(dp.initialState());

        // Exercise the updateState method
        userDao.updateState(updateUser);

        // Read updated User from DB
        User verifyUpdateUser = userDao.read(updateUser.getId());
        assertNotNull(verifyUpdateUser);

        // Make sure it has same user Id
        assertEquals(verifyUpdateUser.getId(), createUser.getId());

        // Verify that CurrentState got updated
        assertEquals(updateUser.getCurrentState(), verifyUpdateUser.getCurrentState());

        // Verify that other fields are in fact intact
        assertEquals(createUser.getFirstName(), verifyUpdateUser.getFirstName());
        assertEquals(createUser.getLastName(), verifyUpdateUser.getLastName());
        assertEquals(createUser.getPantherId(), verifyUpdateUser.getPantherId());
        assertEquals(createUser.getEmail(), verifyUpdateUser.getEmail());
    }

    /**
     * Verify that {@link UserDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        User createUser = TestDataUtility.userWithTestValues();
        userDao.create(createUser);
        assertNotNull(createUser.getId());

        userDao.delete(createUser.getId());

        User verifyDeleteUser = userDao.read(createUser.getId());
        assertNull(verifyDeleteUser);
    }

    /**
     * Verify that {@link UserDao#delete} is working correctly when a request for a non-existent {@link User #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentUser() {
        Long id = TestDataUtility.randomLong();
        userDao.delete(id);
    }
}
