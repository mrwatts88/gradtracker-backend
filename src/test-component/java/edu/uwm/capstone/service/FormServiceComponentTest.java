package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.util.TestDataUtility;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FormServiceComponentTest {
    @Autowired
    private FormService formService;

    @Autowired
    private FormDefinitionDao formDefinitionDao;

    @Autowired
    private UserDao userDao;

    private List<Form> formsToCleanup = new ArrayList<>();
    private List<FormDefinition> formDefsToCleanup = new ArrayList<>();
    private List<User> usersToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(formService);
    }

    @After
    public void teardown() {
        formsToCleanup.forEach(form -> formService.delete(form.getId()));
        formsToCleanup.clear();

        formDefsToCleanup.forEach(formDef -> formDefinitionDao.delete(formDef.getId()));
        formDefsToCleanup.clear();

        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
    }

    /**
     * Verify that {@link FormService#create} is working correctly.
     */
    @Test
    public void create() {
        FormDefinition formDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(formDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);
        
        Form createForm = TestDataUtility.formWithTestValues(formDef, user.getId());
        formsToCleanup.add(createForm);

        formService.create(createForm);
        assertNotNull(createForm.getId());
        assertNotNull(createForm.getCreatedDate());
        Form verifyForm = formService.read(createForm.getId());
        assertEquals(createForm, verifyForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullForm() {
        formService.create(null);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFormId() {
        FormDefinition formDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(formDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(formDef, user.getId());
        createForm.setId(TestDataUtility.randomLong());
        formService.create(createForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form} with a null list of fields is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFields() {
        FormDefinition formDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(formDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(formDef, user.getId());
        createForm.setFields(null);
        formService.create(createForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormNullUserId() {
        FormDefinition formDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(formDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(formDef, user.getId());
        createForm.setUserId(null);
        formService.create(createForm);
    }

    // TODO finish remaining tests
//    /**
//     * Verify that {@link FormService#read} is working correctly.
//     */
//    @Test
//    public void read() {
//        Form sampleform = TestDataUtility.formWithTestValues();
//        formService.create(sampleform);
//        assertNotNull(sampleform.getId());
//        formsToCleanup.add(sampleform);
//
//        Form readform = formService.read(sampleform.getId());
//        assertNotNull(readform);
//        assertEquals(sampleform.getId(), readform.getId());
//        assertEquals(sampleform, readform);
//    }
//
//    /**
//     * Verify that {@link FormService#read} is working correctly when a request for a non-existent {@link Form #id} is made.
//     */
//    @Test(expected = RuntimeException.class)
//    public void readNonExistentform() {
//        // create a random user id that will not be in our local database
//        Long id = TestDataUtility.randomLong();
//        Form Sampleform = formService.read(id);
//        assertNull(Sampleform);
//        assertNull(formService.readAll());
//    }
//
//    /**
//     * Verify that all {@link FormService#readAll} is working correctly,
//     */
//    @Test
//    public void readAll() {
//        List<Form> persistedforms = new ArrayList<>();
//        int randInt = TestDataUtility.randomInt(0, 1);
//        for (int i = 0; i < randInt; i++) {
//            Form form = TestDataUtility.formWithTestValues();
//            formService.create(form);
//            formsToCleanup.add(form);
//            persistedforms.add(form);
//        }
//
//        assertEquals(persistedforms, formService.readAll());
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly.
//     */
//    @Test
//    public void update() {
//        Form createform = TestDataUtility.formWithTestValues();
//        formService.create(createform);
//        assertNotNull(createform.getId());
//        formsToCleanup.add(createform);
//
//        Form verifyCreateform = formService.read(createform.getId());
//        assertNotNull(verifyCreateform);
//        assertEquals(createform, verifyCreateform);
//
//        Form updateform = TestDataUtility.formWithTestValues();
//        updateform.setId(createform.getId());
//        formService.update(updateform);
//
//        Form verifyUpdateform = formService.read(updateform.getId());
//        assertNotNull(verifyUpdateform);
//        assertEquals(updateform, verifyUpdateform);
//        assertNotEquals(verifyUpdateform, verifyCreateform);
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly when a request for creating a null object is made.
//     */
//    @Test(expected = RuntimeException.class)
//    public void updateNullform() {
//        formService.update(null);
//        assertNull(formService.readAll());
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly when a request for a non-existent {@link Form#id} is made.
//     */
//    @Test(expected = RuntimeException.class)
//    public void updateNonExistentform() {
//        // create a random user id that will not be in our local database
//        Form updateform = TestDataUtility.formWithTestValues();
//        updateform.setId(TestDataUtility.randomLong());
//        formService.update(updateform);
//        assertNull(formService.readAll());
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly when a request for a {@link Form} that contains a value
//     * which exceeds the database configuration is made.
//     */
//    @Test(expected = RuntimeException.class)
//    public void updateformColumnTooLong() {
//        // generate a test user value with a column that will exceed the database configuration
//        Form createform = TestDataUtility.formWithTestValues();
//        formService.create(createform);
//        assertNotNull(createform.getId());
//        formsToCleanup.add(createform);
//
//        Form verifyform = formService.read(createform.getId());
//        assertNotNull(verifyform);
//        assertEquals(createform, verifyform);
//
//        Form updateform = TestDataUtility.formWithTestValues();
//        updateform.setId(createform.getId());
//        updateform.setName(RandomStringUtils.randomAlphabetic(2000));
//        formService.update(updateform);
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly when field definitions are updated
//     * but not exist for form definition.
//     */
//    @Test(expected = RuntimeException.class)
//    public void updateformUnknownFieldDefId() {
//        Form createform = TestDataUtility.formWithTestValues();
//        formService.create(createform);
//        assertNotNull(createform.getId());
//
//        Form verifyCreateform = formService.read(createform.getId());
//        assertNotNull(verifyCreateform);
//        assertEquals(createform, verifyCreateform);
//        formsToCleanup.add(createform);
//
//        Form updateform = new Form();
//        updateform.setName(TestDataUtility.randomAlphabetic(10));
//        List<FieldDefinition> fieldDefinitions = new ArrayList<>();
//        FieldDefinition fd = TestDataUtility.fieldDefWithTestValues();
//        fd.setId(TestDataUtility.randomLong());
//        fieldDefinitions.add(fd);
//        updateform.setFieldDefs(fieldDefinitions);
//        formService.update(updateform);
//    }
//
//    /**
//     * Verify that {@link FormService#update} is working correctly when field definitions are updated
//     * but without FieldDef.
//     */
//    @Test(expected = RuntimeException.class)
//    public void updateFieldDefEmptyFieldDefs() {
//        Form createform = TestDataUtility.formWithTestValues();
//        formService.create(createform);
//        assertNotNull(createform.getId());
//
//        Form verifyCreateform = formService.read(createform.getId());
//        assertNotNull(verifyCreateform);
//        assertEquals(createform, verifyCreateform);
//        formsToCleanup.add(createform);
//
//        Form updateform = TestDataUtility.formWithTestValues();
//        updateform.setId(verifyCreateform.getId());
//        updateform.setFieldDefs(Collections.emptyList());
//        formService.update(updateform);
//    }
//
//    /**
//     * Verify that {@link FormService#delete} is working correctly.
//     */
//    @Test
//    public void delete() {
//        Form createform = TestDataUtility.formWithTestValues();
//        formService.create(createform);
//        assertNotNull(createform.getId());
//
//        Form verifyform = formService.read(createform.getId());
//        assertNotNull(verifyform);
//        assertEquals(createform.getId(), verifyform.getId());
//        assertEquals(createform, verifyform);
//
//        formService.delete(createform.getId());
//    }
//
//    /**
//     * Verify that {@link FormService#delete} is working correctly when a request for a non-existent {@link Form #id} is made.
//     */
//    @Test(expected = RuntimeException.class)
//    public void deleteNonExistentform() {
//        Long id = TestDataUtility.randomLong();
//        formService.delete(id);
//        assertNull(formService.readAll());
//    }
}
