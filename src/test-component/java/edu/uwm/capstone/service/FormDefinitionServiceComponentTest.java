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
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FormDefinitionServiceComponentTest {
    @Autowired
    private FormDefinitionService formDefinitionService;

    private List<FormDefinition> FormDefinitionToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(formDefinitionService);
    }

    @After
    public void teardown() {
        FormDefinitionToCleanup.forEach(user -> formDefinitionService.delete(user.getId()));
        FormDefinitionToCleanup.clear();
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
        FormDefinitionToCleanup.add(createFormDef);
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
    }

    /**
     * Verify that {@link FormDefinitionService#create} is working correctly when a request for a {@link FormDefinition} with a null fieldDef is made.
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
        // generate a test user value with a column that will exceed the database configuration
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
        formDefinitionService.create(sampleFormDef);
        assertNotNull(sampleFormDef.getId());
        FormDefinitionToCleanup.add(sampleFormDef);

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
    }

    /**
     * Verify that {@link FormDefinitionService #update} is working correctly.
     * TODO: FIGURE OUT WHAT SUPPOSED TO HAPPEN IN THE UPDATE METHOD
     */
    @Test
    public void update() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionService.create(createFormDef);
        assertNotNull(createFormDef.getId());
        FormDefinitionToCleanup.add(createFormDef);

        FormDefinition verifyFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyFormDef);
        assertEquals(createFormDef, verifyFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        formDefinitionService.update(updateFormDef);

        FormDefinition verifyUpdateFormDef = formDefinitionService.read(updateFormDef.getId());
        assertNotNull(verifyUpdateFormDef);
        assertEquals(createFormDef.getId(), verifyUpdateFormDef.getId());
        assertEquals(updateFormDef.getName(), verifyUpdateFormDef.getName());
        //assumed do not have to keep checking in depth of the field contents.
        //assumed index started at 0;
        for(int i = 0; i<createFormDef.getFieldDefs().size(); i++)
        {
            assertEquals(createFormDef.getFieldDefs().indexOf(i), verifyFormDef.getFieldDefs().indexOf(i));
        }
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullFormDef() {
        formDefinitionService.update(null);
    }

    /**
     * Verify that {@link FormDefinitionService#update} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentFormDef() {
        // create a random user id that will not be in our local database
        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        formDefinitionService.update(updateFormDef);
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
        FormDefinitionToCleanup.add(createFormDef);

        FormDefinition verifyFormDef = formDefinitionService.read(createFormDef.getId());
        assertNotNull(verifyFormDef);
        assertEquals(createFormDef.getId(), verifyFormDef.getId());
        assertEquals(createFormDef, verifyFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        updateFormDef.setName(RandomStringUtils.randomAlphabetic(2000));
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
    public void deleteNonExistentUser() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        formDefinitionService.delete(id);
    }
}
