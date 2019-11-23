package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.Role;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoleServiceComponentTest {
    @Autowired
    private RoleService roleService;

    private List<Role> rolesToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(roleService);
    }

    @After
    public void teardown() {
        rolesToCleanup.forEach(role -> roleService.delete(role.getId()));
        rolesToCleanup.clear();
    }

    /**
     * Verify that {@link RoleService#create} is working correctly.
     */
    @Test
    public void create() {
        Role createRole = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createRole);

        roleService.create(createRole);

        assertNotNull(createRole.getId());
        assertNotNull(createRole.getName());
        assertNotNull(createRole.getDescription());
        assertNotNull(createRole.getCreatedDate());
    }

    /**
     * Verify that {@link RoleService#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullRole() {
        roleService.create(null);
    }

    /**
     * Verify that {@link RoleService#create} is working correctly when a request for a {@link Role} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullRoleId() {
        Role createRole = TestDataUtility.roleWithTestValues();
        createRole.setId(TestDataUtility.randomLong());
        roleService.create(createRole);
    }

    /**
     * Verify that {@link RoleService#create} is working correctly when a request for a {@link Role} with a id already exist in the DB.
     */
    @Test(expected = RuntimeException.class)
    public void createExistRoleId() {
        Role createRole = TestDataUtility.roleWithTestValues();

        roleService.create(createRole);
        rolesToCleanup.add(createRole);

        assertNotNull(createRole.getId());
        assertNotNull(createRole.getCreatedDate());

        Role createRepeatRoleId = TestDataUtility.roleWithTestValues();
        createRepeatRoleId.setId(createRole.getId());
        roleService.create(createRole);
    }

    /**
     * Verify that {@link RoleService#create} is working correctly when a request for a {@link Role} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createRoleColumnTooLong() {
        Role createUser = TestDataUtility.roleWithTestValues();
        createUser.setName(RandomStringUtils.randomAlphabetic(2000));
        roleService.create(createUser);
    }

    /**
     * Verify that {@link RoleService#read} is working correctly.
     */
    @Test
    public void read() {
        Role createRole = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createRole);

        roleService.create(createRole);
        assertNotNull(createRole.getId());

        Role readRole = roleService.read(createRole.getId());
        assertNotNull(readRole);
        assertEquals(createRole, readRole);
    }

    /**
     * Verify that {@link RoleService#read} is working correctly.
     */
    @Test
    public void readByName() {
        Role createRole = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createRole);

        roleService.create(createRole);
        assertNotNull(createRole.getId());

        Role readRole = roleService.readByName(createRole.getName());
        assertNotNull(readRole);
        assertEquals(createRole, readRole);
    }

    /**
     * Verify that {@link RoleService#read} is working correctly when a request for a non-existent {@link Role} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentRole() {
        // create a random role id that will not be in our local database
        Long id = TestDataUtility.randomLong();
        roleService.read(id);
    }

    /**
     * Verify that {@link RoleService#update} is working correctly.
     */
    //TODO: find the way to fix the update method and implement its tests.
    @Test
    public void update() {
        Role createUser = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createUser);

        roleService.create(createUser);
        assertNotNull(createUser.getId());

        Role verifyCreateUser = roleService.read(createUser.getId());
        assertNotNull(verifyCreateUser);
        assertEquals(createUser, verifyCreateUser);

        Role updateUser = TestDataUtility.roleWithTestValues();
        updateUser.setId(createUser.getId());
        roleService.update(updateUser);

        Role verifyUpdateUser = roleService.read(updateUser.getId());
        assertNotNull(verifyUpdateUser);

        assertEquals(createUser.getId(), verifyUpdateUser.getId());
        assertEquals(updateUser.getName(), verifyUpdateUser.getName());
        assertEquals(updateUser.getDescription(), verifyUpdateUser.getDescription());


        assertNotEquals(verifyCreateUser.getName(), verifyUpdateUser.getName());
        assertNotEquals(verifyCreateUser.getDescription(), verifyUpdateUser.getDescription());

    }

    /**
     * Verify that {@link RoleService#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullRole() {
        roleService.update(null);
    }

    /**
     * Verify that {@link RoleService#update} is working correctly when a request for a non-existent {@link Role} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentRole() {
        // create a random role id that will not be in our local database
        Role updateRole = TestDataUtility.roleWithTestValues();
        updateRole.setId(TestDataUtility.randomLong());
        roleService.update(updateRole);
    }


    /**
     * Verify that {@link RoleService#update} is working correctly when a request for a {@link Role} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateRoleColumnTooLong() {
        Role createRole = TestDataUtility.roleWithTestValues();
        rolesToCleanup.add(createRole);

        roleService.create(createRole);
        assertNotNull(createRole.getId());

        Role updateRole = TestDataUtility.roleWithTestValues();
        updateRole.setId(createRole.getId());
        updateRole.setName(RandomStringUtils.randomAlphabetic(2000));
        roleService.update(updateRole);
    }

    /**
     * Verify that {@link RoleService#delete} is working correctly.
     */
    @Test
    public void delete() {
        Role createRole = TestDataUtility.roleWithTestValues();
        roleService.create(createRole);
        assertNotNull(createRole.getId());

        roleService.delete(createRole.getId());
    }

    /**
     * Verify that {@link RoleService#delete} is working correctly when a request for a non-existent {@link Role} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentRole() {
        Long id = TestDataUtility.randomLong();
        roleService.delete(id);
    }
}
