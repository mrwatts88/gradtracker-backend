package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.util.TestDataUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static edu.uwm.capstone.sql.dao.BaseRowMapper.dateFromJavaTime;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.javaTimeFromDate;
import static edu.uwm.capstone.util.TestDataUtility.randomLocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class FormDaoRowMapperComponentTest {
    @Autowired
    FormDaoRowMapper formDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(formDaoRowMapper);
    }

    /**
     * Verify that {@link FormDaoRowMapper.FormColumnType#values()} is working correctly.
     */
    @Test
    public void userColumnType() {
        for (FormDaoRowMapper.FormColumnType columnType : FormDaoRowMapper.FormColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link FormDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a Form object with test values
        Form sampleForm = new Form();
        sampleForm.setFormDefId(TestDataUtility.randomLong());
        sampleForm.setUserId(TestDataUtility.randomLong());
        sampleForm.setApproved(TestDataUtility.randomBoolean());

        sampleForm.setId(TestDataUtility.randomLong());
        sampleForm.setCreatedDate(randomLocalDateTime());
        sampleForm.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = formDaoRowMapper.mapObject(sampleForm);
        assertNotNull(mapObject);

        assertEquals(sampleForm.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));

        assertEquals(sampleForm.getFormDefId(), mapObject.get(FormDaoRowMapper.FormColumnType.FORM_DEF_ID.getColumnName()));
        assertEquals(sampleForm.getUserId(), mapObject.get(FormDaoRowMapper.FormColumnType.USER_ID.getColumnName()));
        assertEquals(sampleForm.getApproved(), mapObject.get(FormDaoRowMapper.FormColumnType.APPROVED.getColumnName()));

        assertEquals(sampleForm.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(sampleForm.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link FormDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a Form object with test values
        Form sampleForm = new Form();
        sampleForm.setFormDefId(TestDataUtility.randomLong());
        sampleForm.setUserId(TestDataUtility.randomLong());
        sampleForm.setApproved(TestDataUtility.randomBoolean());

        sampleForm.setId(TestDataUtility.randomLong());
        sampleForm.setCreatedDate(randomLocalDateTime());
        sampleForm.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(sampleForm.getId());
        when(resultSet.getLong(FormDaoRowMapper.FormColumnType.FORM_DEF_ID.getColumnName())).thenReturn(sampleForm.getFormDefId());
        when(resultSet.getLong(FormDaoRowMapper.FormColumnType.USER_ID.getColumnName())).thenReturn(sampleForm.getUserId());
        when(resultSet.getBoolean(FormDaoRowMapper.FormColumnType.APPROVED.getColumnName())).thenReturn(sampleForm.getApproved());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleForm.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleForm.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        Form verifyFormDef = formDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyFormDef);

        assertEquals(sampleForm.getId(), verifyFormDef.getId());
        assertEquals(sampleForm.getFormDefId(), verifyFormDef.getFormDefId());
        assertEquals(sampleForm.getUserId(), verifyFormDef.getUserId());
        assertEquals(sampleForm.getApproved(), verifyFormDef.getApproved());
        assertEquals(sampleForm.getCreatedDate(), verifyFormDef.getCreatedDate());
        assertEquals(sampleForm.getUpdatedDate(), verifyFormDef.getUpdatedDate());
    }
}
