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
        formDefinitionToCleanup.add(createFormDef);

        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());
        assertNotNull(createFormDef.getCreatedDate());

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
        createFormDef.setId(TestDataUtility.randomLong());
        formDefinitionService.create(createFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} with a null list of field definitions is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFieldDef() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        createFormDef.setFieldDefs(null);
        formDefinitionService.create(createFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormDefNameColumnTooLong() {
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        sampleFormDef.setName(RandomStringUtils.randomAlphabetic(2000));
        formDefinitionService.create(sampleFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#read} is working correctly.
     */
    @Test
    public void read() {
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionToCleanup.add(sampleFormDef);

        formDefinitionService.create(sampleFormDef);
        assertNotNull(sampleFormDef.getId());

        FormDefinition readFormDef = formDefinitionService.read(sampleFormDef.getId());
        assertNotNull(readFormDef);
        assertEquals(sampleFormDef, readFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#read} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentFormDef() {
        Long id = TestDataUtility.randomLong();
        formDefinitionService.read(id);
    }

    /**
     * Verify that all {@link FormDefinitionService#readAll} is working correctly,
     */
    @Test
    public void readAll() {
        List<FormDefinition> persistedFormDefs = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
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
        formDefinitionToCleanup.add(createFormDef);

        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

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
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for a non-existent {@link FormDefinition#id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentFormDef() {
        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(TestDataUtility.randomLong());
        formDefinitionService.update(updateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormDefColumnTooLong() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionToCleanup.add(createFormDef);

        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

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
        formDefinitionToCleanup.add(createFormDef);

        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

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
        formDefinitionToCleanup.add(createFormDef);

        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
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

        formDefinitionService.delete(createFormDef.getId());
    }

    /**
     * Verify that {@link FormDefinitionService#delete} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentFormDef() {
        Long id = TestDataUtility.randomLong();
        formDefinitionService.delete(id);
        assertNull(formDefinitionService.readAll());
    }
}
