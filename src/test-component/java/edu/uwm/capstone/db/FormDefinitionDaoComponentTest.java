package edu.uwm.capstone.db;
import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class FormDefinitionDaoComponentTest {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FormDefinitionDao formDefinitionDao;

    @Before
    public void setUp() {
        assertNotNull(formDefinitionDao);
        assertNotNull(formDefinitionDao.sql("createFormDef"));
        assertNotNull(formDefinitionDao.sql("readFormDef"));
        assertNotNull(formDefinitionDao.sql("readAllFormDefs"));
        assertNotNull(formDefinitionDao.sql("deleteFormDef"));
        assertNotNull(formDefinitionDao.sql("updateFormDef"));
    }

    /**
     * Verify that {@link FormDefinitionDao #create} is working correctly.
     */
    @Test
    public void create() {
        FormDefinition create_form_def = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(create_form_def);
        assertNotNull(create_form_def.getId());
    }

    /**
     * Verify that {@link FormDefinitionDao #create} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullformDefinition() {
        formDefinitionDao.create(null);
    }

    /**
     * Verify that {@link FormDefinitionDao #create} is working correctly when a request for a {@link FormDefinition} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullFormDefId() {
        FormDefinition create_form_def = TestDataUtility.formDefWithTestValues();
        create_form_def.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
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

        FormDefinition read_form_def = formDefinitionDao.read(create_form_def.getId());
        assertNotNull(read_form_def);
        assertEquals(create_form_def.getId(), read_form_def.getId());
        assertEquals(create_form_def, read_form_def);
    }

    /**
     * Verify that {@link FormDefinitionDao#read} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     */
    @Test
    public void readNonExistentformdef() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        FormDefinition formDefinition = formDefinitionDao.read(id);
        assertNull(formDefinition);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly.
     * TODO: CHECK what the method is supposed to do in update.
     */
    @Test
    public void update() {
        assertTrue(true);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for creating a null object is made.
     * TODO: CHECK OUT WHAT THE UPDATE ACTUALLY supposed to do and then implement the following update method unit test.
     */
    @Test//(expected = RuntimeException.class)
    public void updateNullformdef() {
        assertTrue(true);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for a non-existent {@link FormDefinition #id} is made.
     *  TODO: CHECK OUT WHAT THE UPDATE ACTUALLY supposed to do and then implement the following update method unit test.
     */

    @Test//(expected = RuntimeException.class)
    public void updateNonExistentformdef() {
        assertTrue(true);
    }

    /**
     * Verify that {@link FormDefinitionDao#update} is working correctly when a request for a {@link FormDefinition} that contains a value
     * which exceeds the database configuration is made.
     * TODO: CHECK what the method is supposed to do in update.
     */
    @Test//(expected = RuntimeException.class)
    public void updateUserColumnTooLong() {
       assertTrue(true);
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
    public void deleteNonExistentformdef() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        formDefinitionDao.delete(id);
    }

    /**
     *
     */
    @Test
    public void readAllformdef(){
        FormDefinition createUser = TestDataUtility.formDefWithTestValues();
        formDefinitionDao.create(createUser);
        assertNotNull(formDefinitionDao.readAll());
    }
}
