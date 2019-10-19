package edu.uwm.capstone.db;
import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.exception.DaoException;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)

public class FormDaoComponentTest {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FormDao formDao;

    //we do not have to set up the cleanUpList as the Form are not allow to be deleted.

    @Before
    public void setUp() {
        assertNotNull(formDao);
        assertNotNull(formDao.sql("createForm"));
        assertNotNull(formDao.sql("readForm"));
        assertNotNull(formDao.sql("readAllForm"));
        //TODO: CONSIDER TO REMOVE THE DELETE FUNCTIONALITY FOR FORMS IN THE SQL FILE
//        assertNotNull(formDao.sql("deleteForm"));
        assertNotNull(formDao.sql("updateForm"));
    }

    //Since we do not have to delete anything we do not need @TearDown methods.

    /**
     * Verify that {@link FormDao#create} is working correctly.
     */
    @Test
    public void create() {
//        Form createForm = TestDataUtility.formWithTestValues();
//        formDao.create(createForm);
//        Form verifyCreateForm = formDao.read(createForm.getId());
//        assertNotNull(verifyCreateForm);
//        assertEquals(createForm, verifyCreateForm);
        Form a = new Form();
        a.setFormDefId(123L);
        a.setUserId(123L);
        a.setApproved(true);
        a.setId(null);
        List<Field> b = new ArrayList<>();
        Field c = new Field();
        c.setData("fads");
        c.setFormId(431L);
        c.setFieldIndex(0);
        c.setId(12345L);
        a.setFields(b);
        formDao.create(a);
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
        Form create_form = TestDataUtility.formWithTestValues();
        create_form.setId(new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong());
        formDao.create(create_form);
    }

    /**
     * Verify that {@link FormDao#create} is working correctly when a request for a {@link Form} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createFormColumnTooLong() {
        Form create_form = TestDataUtility.formWithTestValues();
        create_form.getFields().get(1).setData(RandomStringUtils.randomAlphabetic(2000));
        formDao.create(create_form);
    }

    /**
     * Verify that {@link FormDao#read} is working correctly.
     */
    @Test
    public void read() {
        Form create_form = TestDataUtility.formWithTestValues();
        formDao.create(create_form);
        assertNotNull(create_form.getId());
        Form read_form = formDao.read(create_form.getId());
        assertNotNull(read_form);
        assertEquals(create_form.getId(), read_form.getId());
        assertEquals(create_form, read_form);
    }

    /**
     * Verify that {@link FormDao#read} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test
    public void readNonExistentForm() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        Form form = formDao.read(id);
        assertNull(form);
    }

    /**
     * Verify that all {@link FormDao#readAll} is working correctly,
     */
    @Test
    public void readAllForm() {
        List<Form> persistedForms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            Form form = TestDataUtility.formWithTestValues();
            formDao.create(form);
            persistedForms.add(form);
        }

        assertEquals(persistedForms, formDao.readAll());
    }

    /**
     * Verify that {@link FormDao#update} is working correctly.
     */
    @Test
    public void update() {
        Form createForm = TestDataUtility.formWithTestValues();
        formDao.create(createForm);
        assertNotNull(createForm.getId());

        Form verifyCreateForm = formDao.read(createForm.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(createForm, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues();
        updateForm.setId(createForm.getId());
        formDao.update(updateForm);

        Form verifyUpdateForm = formDao.read(updateForm.getId());

        assertNotNull(verifyUpdateForm);
        assertEquals(updateForm, verifyUpdateForm);
        assertNotEquals(verifyUpdateForm.getUserId(), verifyCreateForm.getUserId());
        assertNotEquals(verifyUpdateForm.getFields(), verifyCreateForm.getFields());
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when one field is kept
     * but all others are replaced by new fields.
     */
    @Test
    public void updateFormLeaveOneFieldTheSame() {
        Form createForm = TestDataUtility.formWithTestValues();
        formDao.create(createForm);
        assertNotNull(createForm.getId());

        Form verifyCreateForm = formDao.read(createForm.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(createForm, verifyCreateForm);

        Form updateForm = TestDataUtility.formWithTestValues();
        updateForm.setId(createForm.getId());
        Field fd = verifyCreateForm.getFields().get(1);
        updateForm.getFields().add(fd); // add Field from created Form
        formDao.update(updateForm);

        Form verifyUpdateForm = formDao.read(updateForm.getId());

        assertNotNull(verifyUpdateForm);
        assertNotEquals(verifyUpdateForm, verifyCreateForm);
        assertTrue(verifyCreateForm.getFields().contains(fd));
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when fields are updated
     * but not removed or added to the form.
     */
    @Test
    public void updateFormLeaveAllFieldIds() {
        Form createForm = TestDataUtility.formWithTestValues();
        formDao.create(createForm);
        assertNotNull(createForm.getId());

        Form verifyCreateForm = formDao.read(createForm.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(createForm, verifyCreateForm);


        Form updateForm = new Form();
        updateForm.setFields(new ArrayList<>());

        updateForm.setId(createForm.getId());

        for (Field fd : verifyCreateForm) {
            Field updatedField = TestDataUtility.fieldWithTestValues();
            updatedField.setId(fd.getId());
            updateForm.getFields().add(updatedField);
        }

        formDao.update(updateForm);

        Form verifyUpdateForm = formDao.read(updateForm.getId());

        assertNotNull(verifyUpdateForm);

        Field createdField, updatedField;
        for (int i = 0; i < verifyCreateForm.getFields().size(); i++) {
            createdField = verifyCreateForm.getFields().get(i);
            updatedField = verifyUpdateForm.getFields().get(i);
            assertEquals(createdField.getId(), updatedField.getId());
            assertNotEquals(createdField, updatedField);
        }
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
        Form updateForm = TestDataUtility.formWithTestValues();
        updateForm.setId(new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong());
        formDao.update(updateForm);
    }

    /**
     * Verify that {@link FormDao#update} is working correctly when a request for a {@link Form} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateFormColumnTooLong() {
        Form createForm = TestDataUtility.formWithTestValues();
        formDao.create(createForm);
        assertNotNull(createForm.getId());

        Form verifyCreateForm = formDao.read(createForm.getId());
        assertNotNull(verifyCreateForm);
        assertEquals(createForm, verifyCreateForm);

        Form updateUser = TestDataUtility.formWithTestValues();
        updateUser.setId(createForm.getId());
        updateUser.getFields().get(1).setData(RandomStringUtils.randomAlphabetic(2000));
        formDao.update(updateUser);
    }

    /**
     * Verify that {@link FormDao#delete} is working correctly.
     */
    @Test(expected = RuntimeException.class)
    public void delete() {
        Form sample_form = TestDataUtility.formWithTestValues();
        formDao.create(sample_form);
        assertNotNull(sample_form.getId());

        Form verify_form = formDao.read(sample_form.getId());
        assertNotNull(verify_form);
        assertEquals(sample_form.getId(), verify_form.getId());
        assertEquals(sample_form, verify_form);

        formDao.delete(sample_form.getId());
    }

    /**
     * Verify that {@link FormDao#delete} is working correctly when a request for a non-existent {@link Form #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentForm() {
        Long id = new Random().longs(10000L, Long.MAX_VALUE).findAny().getAsLong();
        formDao.delete(id);
    }
}
