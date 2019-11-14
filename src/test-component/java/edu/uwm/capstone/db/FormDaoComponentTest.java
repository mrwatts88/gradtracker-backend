package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.*;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class FormDaoComponentTest {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FormDao formDao;

    @Autowired
    FormDefinitionDao formDefinitionDao;

    @Autowired
    private UserDao userDao;

    private List<Form> formsToCleanup = new ArrayList<>();
    private List<FormDefinition> formDefsToCleanup = new ArrayList<>();
    private List<User> usersToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(formDao);
        assertNotNull(formDao.sql("createForm"));
        assertNotNull(formDao.sql("readForm"));
        assertNotNull(formDao.sql("readAllForms"));
        assertNotNull(formDao.sql("readAllFormsByUserId"));
        assertNotNull(formDao.sql("deleteForm"));
        assertNotNull(formDao.sql("updateForm"));
    }

    @After
    public void teardown() {
        formsToCleanup.forEach(formDef -> formDao.delete(formDef.getId()));
        formsToCleanup.clear();

        formDefsToCleanup.forEach(formDef -> formDefinitionDao.delete(formDef.getId()));
        formDefsToCleanup.clear();

        usersToCleanup.forEach(user -> userDao.delete(user.getId()));
        usersToCleanup.clear();
    }

    /**
     * Verify that {@link FormDao#create} is working correctly.
     */
    @Test
    public void create() {
        // need a form definition in the db connected to the form
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        // need a user in the db connected to the form
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        formsToCleanup.add(createForm);

        formDao.create(createForm);
        assertNotNull(createForm.getId());
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a {@link FormDefinition} doesn't exist for that {@link Form}.
     */
    @Test(expected = RuntimeException.class)
    public void createFormNonExistentFormDef() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setFormDefId(TestDataUtility.randomLong());

        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a {@link FormDefinition} doesn't exist for that {@link Form}.
     */
    @Test(expected = RuntimeException.class)
    public void createFormNonExistentUser() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, TestDataUtility.randomLong());

        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form}
     * containing a field with a null field definition id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonExistentFieldDef() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.getFields().get(0).setFieldDefId(TestDataUtility.randomLong());

        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullForm() {
        formDao.create(null);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFormId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setId(TestDataUtility.randomLong());
        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form}
     * containing a field with a non null field id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFieldId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.getFields().get(0).setId(TestDataUtility.randomLong());
        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form} with a null form definition id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFormDefId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setFormDefId(null);
        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form} with a null user id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullUserId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);
        Form createForm = TestDataUtility.formWithTestValues(createFormDef, null);
        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form}
     * containing a field with a null field definition id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFieldDefId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.getFields().get(0).setFieldDefId(null);
        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormColumnTooLong() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.getFields().get(0).setData(TestDataUtility.randomAlphabetic(2000));

        formDao.create(createForm);
    }

    /**
     * Verify that {@link FormDao#read} is working correctly.
     */
    @Test
    public void read() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        formsToCleanup.add(createForm);

        formDao.create(createForm);
        assertNotNull(createForm.getId());

        Form readForm = formDao.read(createForm.getId());
        assertNotNull(readForm);
        assertEquals(createForm, readForm);

        assertEquals(createFormDef.getName(), readForm.getName());

        HashMap<Long, FieldDefinition> map = new HashMap<>();
        createFormDef.forEach((fd) -> map.put(fd.getId(), fd));

        for (Field f : readForm.getFields()) {
            assertEquals(map.get(f.getFieldDefId()).getLabel(), f.getLabel());
            assertEquals(map.get(f.getFieldDefId()).getFieldIndex(), f.getFieldIndex());
        }
    }

    /**
     * Verify that {@link FormDao#read} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test
    public void readNonExistentForm() {
        Long id = TestDataUtility.randomLong();
        Form form = formDao.read(id);
        assertNull(form);
    }

    /**
     * Verify that {@link FormDao#readAll} is working correctly,
     */
    @Test
    public void readAllForms() {
        List<Form> persistedForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
            formsToCleanup.add(createForm);

            formDao.create(createForm);
            persistedForms.add(createForm);
        }
        assertEquals(persistedForms, formDao.readAll());
    }

    /**
     * Verify that {@link FormDao#readAllByFormDefId} is working correctly,
     */
    @Test
    public void readAllFormsByFormDefId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);
        Long formDefId = createFormDef.getId();
        
        List<Form> persistedForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {

            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
            formsToCleanup.add(createForm);

            formDao.create(createForm);
            persistedForms.add(createForm);
        }

        // create more forms
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            // need a user in the db connected to the form
            User user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            formsToCleanup.add(formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }
        assertEquals(persistedForms, formDao.readAllByFormDefId(formDefId));
    }

    /**
     * Verify that {@link FormDao#readAllByUserId} is working correctly,
     */
    @Test
    public void readAllFormsByUserId() {
        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);
        Long userId = user.getId();

        List<Form> persistedForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
            formsToCleanup.add(createForm);

            formDao.create(createForm);
            persistedForms.add(createForm);
        }

        // create more forms
        for (int i = 0; i < randInt; i++) {
            // need a form definition in the db connected to the form
            FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
            formDefsToCleanup.add(createFormDef);

            // need a user in the db connected to the form
            user = userDao.create(TestDataUtility.userWithTestValues());
            usersToCleanup.add(user);

            formsToCleanup.add(formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }
        assertEquals(persistedForms, formDao.readAllByUserId(userId));
    }

    /**
     * Verify that {@link FormDao#update} is working correctly.
     */
    @Test
    public void update() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        formsToCleanup.add(createForm);

        // update the Form
        Form updatedForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        updatedForm.setId(createForm.getId());
        formDao.update(updatedForm);

        Form verifyUpdateForm = formDao.read(updatedForm.getId());

        assertNotNull(verifyUpdateForm);
        assertEquals(updatedForm, verifyUpdateForm);
        assertEquals(createForm.getFormDefId(), verifyUpdateForm.getFormDefId());
        assertNotEquals(createForm.getFields(), verifyUpdateForm.getFields());
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when field definitions are updated
     * but not exist for form definition.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormUnknownFieldDefId() {

        //create form definitions
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(formDefinition);
        formDefsToCleanup.add(formDefinition);

        //create user
        User user = TestDataUtility.userWithTestValues();
        userDao.create(user);
        usersToCleanup.add(user);

        //create form
        Form form = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        formDao.create(form);
        formsToCleanup.add(form);

        assertNotNull(form.getId());

        Form verifyCreateForm = formDao.read(form.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(form, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues(formDefinition, user.getId());

        List<Field> fieldDefinitions = new ArrayList<>();
        Field fd = TestDataUtility.fieldWithTestValues(TestDataUtility.fieldDefWithTestValues());
        fieldDefinitions.add(fd);
        updateForm.setFields(fieldDefinitions);
        formDao.update(updateForm);
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullForm() {
        formDao.update(null);
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when a request for a non-existent {@link Form #id} is made.
     */

    @Test(expected = RuntimeException.class)
    public void updateNonExistentForm() {
        //create form definitions
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(formDefinition);
        formDefsToCleanup.add(formDefinition);

        //create user
        User user = TestDataUtility.userWithTestValues();
        userDao.create(user);
        usersToCleanup.add(user);

        Form updateForm = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        updateForm.setId(TestDataUtility.randomLong());
        formDao.update(updateForm);
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when a request for a {@link Form} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormColumnTooLong() {
        //create form definitions
        FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(formDefinition);
        formDefsToCleanup.add(formDefinition);

        //create user
        User user = TestDataUtility.userWithTestValues();
        userDao.create(user);
        usersToCleanup.add(user);

        Form form = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        formDao.create(form);
        formsToCleanup.add(form);
        assertNotNull(form.getId());

        Form verifyCreateForm = formDao.read(form.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(form, verifyCreateForm);

        Form updateUser = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        updateUser.setId(form.getId());
        updateUser.getFields().get(0).setData(TestDataUtility.randomAlphabetic(2000));
        formDao.update(updateUser);
    }

    /**
     * Verify that {@link FormDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formDao.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        assertNotNull(createForm.getId());

        formDao.delete(createForm.getId());
    }

    /**
     * Verify that {@link FormDao#delete} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentForm() {
        formDao.delete(TestDataUtility.randomLong());
    }
}
