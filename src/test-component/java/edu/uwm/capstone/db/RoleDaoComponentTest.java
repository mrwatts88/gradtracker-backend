package edu.uwm.capstone.db;
import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.Role;
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

public class RoleDaoComponentTest {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RoleDao roleDao;

    private List<Role> rolesToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(roleDao);
        assertNotNull(roleDao.sql("createRole"));
        assertNotNull(roleDao.sql("readRoleById"));
        assertNotNull(roleDao.sql("readRoleAuthoritiesByRoleId"));
        assertNotNull(roleDao.sql("readAllRoles"));
        assertNotNull(roleDao.sql("readRoleByName"));
        assertNotNull(roleDao.sql("updateRole"));
        assertNotNull(roleDao.sql("deleteUserRolesByRoleId"));
        assertNotNull(roleDao.sql("deleteRoleAuthoritiesByRoleId"));
        assertNotNull(roleDao.sql("deleteRole"));
    }

    @After
    public void teardown() {
        rolesToCleanup.forEach(role -> roleDao.delete(role.getId()));
        rolesToCleanup.clear();
    }

    /**
     * Verify that {@link RoleDao#create} is working correctly.
     */
    @Test
    public void create() {
        Role createRole = TestDataUtility.roleWithTestValues();
        roleDao.create(createRole);
        assertNotNull(createRole.getId());
        rolesToCleanup.add(createRole);
    }

    /**
     * Verify that {@link RoleDao#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullRole() {
        roleDao.create(null);
    }

    /**
     * Verify that {@link RoleDao#create} is working correctly when a request for a {@link Role} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullRoleId() {
        Role createUser = TestDataUtility.roleWithTestValues();
        createUser.setId(TestDataUtility.randomLong());
        roleDao.create(createUser);
    }

    /**
     * Verify that {@link RoleDao#create} is working correctly when a request for a {@link Role} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createRoleColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        Role createUser = TestDataUtility.roleWithTestValues();
        createUser.setName(TestDataUtility.randomAlphabetic(2000));
        roleDao.create(createUser);
    }

    /**
     * Verify that {@link RoleDao#read} is working correctly.
     */
    @Test
    public void read() {
        Role createRole = TestDataUtility.roleWithTestValues();
        roleDao.create(createRole);
        assertNotNull(createRole.getId());
        rolesToCleanup.add(createRole);

        Role readRole = roleDao.read(createRole.getId());
        assertNotNull(readRole);
        assertEquals(createRole, readRole);
    }

    /**
     * Verify that {@link RoleDao#readByName(String)} is working correctly.
     */
    @Test
    public void readByName() {
        Role createRole = TestDataUtility.roleWithTestValues();
        roleDao.create(createRole);
        assertNotNull(createRole.getId());
        rolesToCleanup.add(createRole);

        Role readRole = roleDao.readByName(createRole.getName());
        assertNotNull(readRole);
        assertEquals(createRole, readRole);
    }

    /**
     * Verify that {@link RoleDao#read} is working correctly when a request for a non-existent {@link Role #id} is made.
     */
    @Test
    public void readNonExistentRoleById() {
        // create a random user id that will not be in our local database
        Long id = TestDataUtility.randomLong();
        Role role = roleDao.read(id);
        assertNull(role);
    }

    /**
     * Verify that {@link RoleDao#read} is working correctly when a request for a non-existent {@link Role #email} is made.
     */
    @Test
    public void readNonExistentUserByName() {
        // create a random email that will not be in our local database
        String name = TestDataUtility.randomAlphanumeric(20);
        Role user = roleDao.readByName(name);
        assertNull(user);
    }

    /**
     * Verify that {@link RoleDao#readAll()} is working correctly.
     */
    @Test
    public void readAllRoles() {
        List<Role> persistedRoles = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            Role user = TestDataUtility.roleWithTestValues();
            roleDao.create(user);
            persistedRoles.add(user);
            rolesToCleanup.add(user);
        }
        assertEquals(persistedRoles, roleDao.readAll());
    }

    /**
     * Verify that {@link RoleDao#update(Role)} is working correctly.
     */
    //TODO: find the way to fix the update method and implement its tests.

    /**
     * Verify that {@link RoleDao#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullRole() {
        roleDao.update(null);
    }

    /**
     * Verify that {@link RoleDao#update} is working correctly when a request for a non-existent {@link Role #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentRole() {
        // create a random user id that will not be in our local database
        Role updateRole = TestDataUtility.roleWithTestValues();
        updateRole.setId(TestDataUtility.randomLong());
        roleDao.update(updateRole);
    }

    /**
     * Verify that {@link RoleDao#update} is working correctly when a request for a {@link Role} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateRoleColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        Role createUser = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createUser);

        roleDao.create(createUser);
        assertNotNull(createUser.getId());

        Role updateUser = TestDataUtility.roleWithTestValues();
        updateUser.setId(createUser.getId());
        updateUser.setName(TestDataUtility.randomAlphabetic(2000));
        roleDao.update(updateUser);
    }

    /**
     * Verify that {@link RoleDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        Role createRole = TestDataUtility.roleWithTestValues();
        roleDao.create(createRole);
        assertNotNull(createRole.getId());

        roleDao.delete(createRole.getId());

        Role verifyDeleteRole = roleDao.read(createRole.getId());
        assertNull(verifyDeleteRole);
    }

    /**
     * Verify that {@link RoleDao#delete} is working correctly when a request for a non-existent {@link Role #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentRole() {
        Long id = TestDataUtility.randomLong();
        roleDao.delete(id);
    }
}
