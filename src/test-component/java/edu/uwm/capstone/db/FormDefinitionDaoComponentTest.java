package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
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
public class FormDefinitionDaoComponentTest {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FormDefinitionDao formDefinitionDao;

    private List<FormDefinition> formDefsToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(formDefinitionDao);
        assertNotNull(formDefinitionDao.sql("createFormDef"));
        assertNotNull(formDefinitionDao.sql("readFormDef"));
        assertNotNull(formDefinitionDao.sql("readAllFormDefs"));
        assertNotNull(formDefinitionDao.sql("deleteFormDef"));
        assertNotNull(formDefinitionDao.sql("updateFormDef"));
    }

    @After
    public void teardown() {
        formDefsToCleanup.forEach(formDef -> formDefinitionDao.delete(formDef.getId()));
        formDefsToCleanup.clear();
    }

    /**
     * Verify that {@link FormDefinitionDao#create} is working correctly.
     */
    @Test
    public void create() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createFormDef);
        FormDefinition verifyCreateFormDef = formDefinitionDao.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefsToCleanup.add(createFormDef);
    }

    /**
     * Verify that {@link FormDefinitionDao#create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullFormDefinition() {
        formDefinitionDao.create(null);
    }

    /**
     * Verify that {@link FormDefinitionDao#create} is working correctly when a request for a {@link FormDefinition} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFormDefId() {
        FormDefinition create_form_def = TestDataUtility.formDefWithTestValues();
        create_form_def.setId(TestDataUtility.randomLong());
        formDefinitionDao.create(create_form_def);
    }

    /**
     * Verify that {@link FormDefinitionDao#create} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormDefinitionColumnTooLong() {
        FormDefinition create_form_def = TestDataUtility.formDefWithTestValues();
        create_form_def.setName(RandomStringUtils.randomAlphabetic(2000));
        formDefinitionDao.create(create_form_def);
    }

    /**
     * Verify that {@link FormDefinitionDao#read} is working correctly.
     */
    @Test
    public void read() {
        FormDefinition create_form_def = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(create_form_def);
        assertNotNull(create_form_def.getId());
        formDefsToCleanup.add(create_form_def);
        FormDefinition read_form_def = formDefinitionDao.read(create_form_def.getId());
        assertNotNull(read_form_def);
        assertEquals(create_form_def.getId(), read_form_def.getId());
        assertEquals(create_form_def, read_form_def);
    }

    /**
     * Verify that {@link FormDefinitionDao#read} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test
    public void readNonExistentFormDef() {
        Long id = TestDataUtility.randomLong();
        FormDefinition formDefinition = formDefinitionDao.read(id);
        assertNull(formDefinition);
    }

    /**
     * Verify that {@link FormDefinitionDao#readAll} is working correctly,
     */
    @Test
    public void readAllFormDefs() {
        List<FormDefinition> persistedFormDefs = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            FormDefinition formDefinition = TestDataUtility.formDefWithTestValues();
            formDefinitionDao.create(formDefinition);
            persistedFormDefs.add(formDefinition);
            formDefsToCleanup.add(formDefinition);
        }
        assertEquals(persistedFormDefs, formDefinitionDao.readAll());
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly.
     */
    @Test
    public void update() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createFormDef);
        assertNotNull(createFormDef.getId());
        formDefsToCleanup.add(createFormDef);

        FormDefinition verifyCreateFormDef = formDefinitionDao.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        formDefinitionDao.update(updateFormDef);

        FormDefinition verifyUpdateFormDef = formDefinitionDao.read(updateFormDef.getId());

        assertNotNull(verifyUpdateFormDef);
        assertEquals(updateFormDef, verifyUpdateFormDef);
        assertNotEquals(verifyUpdateFormDef.getName(), verifyCreateFormDef.getName());
        assertNotEquals(verifyUpdateFormDef.getFieldDefs(), verifyCreateFormDef.getFieldDefs());
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when one field definition is kept
     * but all others are replaced by new field definitions.
     */
    @Test
    public void updateFormDefLeaveOneFieldDefTheSame() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyCreateFormDef = formDefinitionDao.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefsToCleanup.add(createFormDef);

        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(createFormDef.getId());
        FieldDefinition fd = verifyCreateFormDef.getFieldDefs().get(1);
        updateFormDef.getFieldDefs().add(fd); // add FieldDef from created FormDefinition
        formDefinitionDao.update(updateFormDef);

        FormDefinition verifyUpdateFormDef = formDefinitionDao.read(updateFormDef.getId());

        assertNotNull(verifyUpdateFormDef);
        assertNotEquals(verifyUpdateFormDef, verifyCreateFormDef);
        assertTrue(verifyCreateFormDef.getFieldDefs().contains(fd));
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when field definitions are updated
     * but not removed or added to the form definition.
     */
    @Test
    public void updateFormDefLeaveAllFieldDefIds() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyCreateFormDef = formDefinitionDao.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefsToCleanup.add(createFormDef);


        FormDefinition updateFormDef = new FormDefinition();
        updateFormDef.setFieldDefs(new ArrayList<>());

        updateFormDef.setId(createFormDef.getId());
        updateFormDef.setName(verifyCreateFormDef.getName() + "updated");

        for (FieldDefinition fd : verifyCreateFormDef) {
            FieldDefinition updatedFieldDef = TestDataUtility.fieldDefWithTestValues();
            updatedFieldDef.setId(fd.getId());
            updateFormDef.getFieldDefs().add(updatedFieldDef);
        }

        formDefinitionDao.update(updateFormDef);

        FormDefinition verifyUpdateFormDef = formDefinitionDao.read(updateFormDef.getId());

        assertNotNull(verifyUpdateFormDef);
        assertEquals(verifyCreateFormDef.getName() + "updated", verifyUpdateFormDef.getName());

        FieldDefinition createdFieldDef, updatedFieldDef;
        for (int i = 0; i < verifyCreateFormDef.getFieldDefs().size(); i++) {
            createdFieldDef = verifyCreateFormDef.getFieldDefs().get(i);
            updatedFieldDef = verifyUpdateFormDef.getFieldDefs().get(i);
            assertEquals(createdFieldDef.getId(), updatedFieldDef.getId());
            assertNotEquals(createdFieldDef, updatedFieldDef);
        }
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullFormDef() {
        formDefinitionDao.update(null);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */

    @Test(expected = RuntimeException.class)
    public void updateNonExistentFormDef() {
        FormDefinition updateFormDef = TestDataUtility.formDefWithTestValues();
        updateFormDef.setId(TestDataUtility.randomLong());
        formDefinitionDao.update(updateFormDef);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormDefColumnTooLong() {
        FormDefinition createFormDef = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createFormDef);
        assertNotNull(createFormDef.getId());

        FormDefinition verifyCreateFormDef = formDefinitionDao.read(createFormDef.getId());
        assertNotNull(verifyCreateFormDef);
        assertEquals(createFormDef, verifyCreateFormDef);
        formDefsToCleanup.add(createFormDef);

        FormDefinition updateUser = TestDataUtility.formDefWithTestValues();
        updateUser.setId(createFormDef.getId());
        updateUser.setName(RandomStringUtils.randomAlphabetic(2000));
        formDefinitionDao.update(updateUser);
    }

    /**
     * Verify that {@link FormDefinitionDao#delete} is working correctly.
     */
    @Test
    public void delete() {
        FormDefinition sample_form_def = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(sample_form_def);
        assertNotNull(sample_form_def.getId());

        FormDefinition verify_form_def = formDefinitionDao.read(sample_form_def.getId());
        assertNotNull(verify_form_def);
        assertEquals(sample_form_def.getId(), verify_form_def.getId());
        assertEquals(sample_form_def, verify_form_def);

        formDefinitionDao.delete(sample_form_def.getId());

        FormDefinition verify_delete_form_def = formDefinitionDao.read(sample_form_def.getId());
        assertNull(verify_delete_form_def);
    }

    /**
     * Verify that {@link FormDefinitionDao#delete} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentFormDef() {
        Long id = TestDataUtility.randomLong();
        formDefinitionDao.delete(id);
    }
}
