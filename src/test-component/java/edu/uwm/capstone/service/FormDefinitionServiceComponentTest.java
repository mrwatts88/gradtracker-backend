package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.FormDefinition;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FormDefinitionServiceComponentTest {
    @Autowired
    private FormDefinitionService formDefinitionService;

    private List<FormDefinition> formDefinitionToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(formDefinitionService);
    }

    @After
    public void teardown() {
        formDefinitionToCleanup.forEach(formDefinition -> formDefinitionService.delete(formDefinition.getId()));
        formDefinitionToCleanup.clear();
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly.
     */
    @Test
    public void create() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        Long idBefore = createFormDef.getId();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());
        assertNotNull(createFormDef.getCreatedDate());
        assertNotEquals(createFormDef.getId(), idBefore);
        formDefinitionToCleanup.add(createFormDef);
        FormDefinition verifyFormDef = formDefinitionService.read(createFormDef.getId());
        assertEquals(createFormDef, verifyFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFormDef() {
        formDefinitionService.create(null);
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFormDefId() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        createFormDef.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        formDefinitionService.create(createFormDef);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} with a null list of field definitions is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFieldDef() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        createFormDef.setFieldDefs(null);
        formDefinitionService.create(createFormDef);
        formDefinitionToCleanup.add(createFormDef);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormDefNameColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        sampleFormDef.setName(RandomStringUtils.randomAlphabetic(2000));
        formDefinitionService.create(sampleFormDef);
        formDefinitionToCleanup.add(sampleFormDef);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#read} is working correctly.
     */
    @Test
    public void read() {
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(sampleFormDef);
        assertNotNull(sampleFormDef.getId());
        formDefinitionToCleanup.add(sampleFormDef);

        FormDefinition readFormDef = formDefinitionService.read(sampleFormDef.getId());
        assertNotNull(readFormDef);
        assertEquals(sampleFormDef.getId(), readFormDef.getId());
        assertEquals(sampleFormDef, readFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#read} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentFormDef() {
        // create a random user id that will not be in our local database
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        FormDefinition SampleFormDef = formDefinitionService.read(id);
        assertNull(SampleFormDef);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that all {@link FormDefinitionService#readAll} is working correctly,
     */
    @Test
    public void readAll() {
        List<FormDefinition> persistedFormDefs = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(0, 1);
        for (int i = 0; i < randInt; i++) {
            FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
            formDefinitionService.create(formDefinition);
            formDefinitionToCleanup.add(formDefinition);
            persistedFormDefs.add(formDefinition);
        }

        assertEquals(persistedFormDefs, formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly.
     */
    @Test
    public void update() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());
        formDefinitionToCleanup.add(createFormDef);

        FormDefinition verifyCreateFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        formDefinitionService.update(updateFormDef);

        FormDefinition verifyUpdateFormDef = formDefinitionService.read(updateFormDef.getId());
        assertNotNull(verifyUpdateFormDef);
        assertEquals(updateFormDef, verifyUpdateFormDef);
        assertNotEquals(verifyUpdateFormDef, verifyCreateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullFormDef() {
        formDefinitionService.update(null);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for a non-existent {@link FormDefinition#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentFormDef() {
        // create a random user id that will not be in our local database
        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        formDefinitionService.update(updateFormDef);
        assertNull(formDefinitionService.readAll());
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormDefColumnTooLong() {
        // generate a test user value with a column that will exceed the database configuration
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());
        formDefinitionToCleanup.add(createFormDef);

        FormDefinition verifyFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyFormDef);
        assertEquals(createFormDef, verifyFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        updateFormDef.setName(RandomStringUtils.randomAlphabetic(2000));
        formDefinitionService.update(updateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when field definitions are updated
     * but not exist for form definition.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormDefUnknownFieldDefId() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyCreateFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefinitionToCleanup.add(createFormDef);

        FormDefinition updateFormDef = new FormDefinition();
        updateFormDef.setName(TestDataUtility.randomAlphabetic(10));
        List<FieldDefinition> fieldDefinitions = new ArrayList<>();
        FieldDefinition fd = TestDataUtility.fieldDefWithTestValues();
        fd.setId(TestDataUtility.randomLong());
        fieldDefinitions.add(fd);
        updateFormDef.setFieldDefs(fieldDefinitions);
        formDefinitionService.update(updateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when field definitions are updated
     * but without FieldDef.
     */
    @Test(expected = RuntimeException.class)
    public void updateFieldDefEmptyFieldDefs() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyCreateFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefinitionToCleanup.add(createFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(verifyCreateFormDef.getId());
        updateFormDef.setFieldDefs(Collections.emptyList());
        formDefinitionService.update(updateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#delete} is working correctly.
     */
    @Test
    public void delete() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyFormDef);
        assertEquals(createFormDef.getId(), verifyFormDef.getId());
        assertEquals(createFormDef, verifyFormDef);

        formDefinitionService.delete(createFormDef.getId());
    }

    /**
     * Verify that {@link FormDefinitionService#delete} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentFormDef() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        formDefinitionService.delete(id);
        assertNull(formDefinitionService.readAll());
    }
}
