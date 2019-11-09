package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.*;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

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

        Form createForm = formService.create(TestDataUtility.formWithTestValues(formDef, user.getId()));

        assertNotNull(createForm.getId());
        assertNotNull(createForm.getCreatedDate());

        formsToCleanup.add(createForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a {@link FormDefinition} doesn't exist for that {@link Form}.
     */
    @Test(expected = RuntimeException.class)
    public void createFormNonExistentFormDef() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setFormDefId(TestDataUtility.randomLong());
        formService.create(createForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a {@link FormDefinition} doesn't exist for that {@link Form}.
     */
    @Test(expected = RuntimeException.class)
    public void createFormNonExistentUser() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        formService.create(TestDataUtility.formWithTestValues(createFormDef, TestDataUtility.randomLong()));
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form}
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
        formService.create(createForm);
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
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form}
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
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form} with an empty list of fields is made.
     */
    @Test(expected = RuntimeException.class)
    public void createEmptyFields() {
        FormDefinition formDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(formDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(formDef, user.getId());
        createForm.setFields(Collections.emptyList());
        formService.create(createForm);
    }

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form} with a null form definition id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFormDefId() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        createForm.setFormDefId(null);
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

    /**
     * Verify that {@link FormService#create} is working correctly when a request for a {@link Form}
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
        formService.create(createForm);
    }

    /**
     * Verify that {@link FormService#read} is working correctly.
     */
    @Test
    public void read() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = formService.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
        formsToCleanup.add(createForm);

        Form readForm = formService.read(createForm.getId());
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
     * Verify that {@link FormService#read} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentForm() {
        formService.read(TestDataUtility.randomLong());
    }

    /**
     * Verify that {@link FormService#readAll} is working correctly,
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

            Form createForm = formService.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
            formsToCleanup.add(createForm);
            persistedForms.add(createForm);
        }
        assertEquals(persistedForms, formService.readAll());
    }

    /**
     * Verify that {@link FormService#readAllByFormDefId} is working correctly,
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

            formService.create(createForm);
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

            formsToCleanup.add(formService.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }
        assertEquals(persistedForms, formService.readAllByFormDefId(formDefId));
    }

    /**
     * Verify that {@link FormService#readAllByUserId} is working correctly,
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

            Form createForm = formService.create(TestDataUtility.formWithTestValues(createFormDef, user.getId()));
            formsToCleanup.add(createForm);
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

            formsToCleanup.add(formService.create(TestDataUtility.formWithTestValues(createFormDef, user.getId())));
        }
        assertEquals(persistedForms, formService.readAllByUserId(userId));
    }

    /**
     * Verify that {@link FormService#updateFormFields} is working correctly.
     */
    @Test
    public void update() {
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
        formService.create(form);
        formsToCleanup.add(form);

        assertNotNull(form.getId());

        Form verifyCreateForm = formService.read(form.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(form, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        updateForm.setId(form.getId());
        formService.updateFormFields(updateForm.getId(), updateForm.getFields());

        Form verifyUpdateForm = formService.read(updateForm.getId());
        assertNotNull(verifyUpdateForm);
        assertEquals(updateForm, verifyUpdateForm);
        assertNotEquals(verifyUpdateForm, verifyCreateForm);
    }

    /**
     * Verify that {@link FormService#updateFormFields} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullForm() {
        formService.updateFormFields(null, null);
        assertNull(formService.readAll());
    }

    /**
     * Verify that {@link FormService#updateFormFields} is working correctly when a request for a non-existent {@link Form#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentForm() {
        // create a random user id that will not be in our local database

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
        formService.updateFormFields(updateForm.getId(), updateForm.getFields());
        assertNull(formService.readAll());
    }

    /**
     * Verify that {@link FormService#updateFormFields} is working correctly when field definitions are updated
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
        formService.create(form);
        formsToCleanup.add(form);

        assertNotNull(form.getId());

        Form verifyCreateForm = formService.read(form.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(form, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues(formDefinition, user.getId());

        List<Field> fieldDefinitions = new ArrayList<>();
        Field fd = TestDataUtility.fieldWithTestValues(TestDataUtility.fieldDefWithTestValues());
        fieldDefinitions.add(fd);
        updateForm.setFields(fieldDefinitions);
        formService.updateFormFields(updateForm.getId(), updateForm.getFields());
    }

    /**
     * Verify that {@link FormService#updateFormFields} is working correctly when field definitions are updated
     * but without FieldDef.
     */
    @Test(expected = RuntimeException.class)
    public void updateFieldDefEmptyFieldDefs() {

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
        formService.create(form);
        formsToCleanup.add(form);

        assertNotNull(form.getId());

        Form verifyCreateForm = formService.read(form.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(form, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues(formDefinition, user.getId());
        updateForm.setId(verifyCreateForm.getId());
        updateForm.setFields(Collections.emptyList());
        formService.updateFormFields(updateForm.getId(), updateForm.getFields());
    }

    /**
     * Verify that {@link FormService#delete} is working correctly.
     */
    @Test
    public void delete() {
        FormDefinition createFormDef = formDefinitionDao.create(TestDataUtility.formDefWithTestValues());
        formDefsToCleanup.add(createFormDef);

        User user = userDao.create(TestDataUtility.userWithTestValues());
        usersToCleanup.add(user);

        Form createForm = TestDataUtility.formWithTestValues(createFormDef, user.getId());
        formService.create(createForm);
        assertNotNull(createForm.getId());

        formService.delete(createForm.getId());
    }

    /**
     * Verify that {@link FormService#delete} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentForm() {
        Long id = TestDataUtility.randomLong();
        formService.delete(id);
    }
}
