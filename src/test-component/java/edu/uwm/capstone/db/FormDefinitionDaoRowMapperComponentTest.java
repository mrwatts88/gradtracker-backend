package edu.uwm.capstone.db;
import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.FormDefinition;
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
public class FormDefinitionDaoRowMapperComponentTest {
    @Autowired
    FormDefinitionDaoRowMapper formDefinitionDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(formDefinitionDaoRowMapper);
    }

    /**
     * Verify that {@link FormDefinitionDaoRowMapper.FormDefColumnType#values()} is working correctly.
     */
    @Test
    public void userColumnType() {
        for (FormDefinitionDaoRowMapper.FormDefColumnType columnType : FormDefinitionDaoRowMapper.FormDefColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link FormDefinitionDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a FormDefinition object with test values
        FormDefinition sample_form_def = TestDataUtility.formDefWithTestValues();
        sample_form_def.setId(TestDataUtility.randomLong());
        sample_form_def.setCreatedDate(randomLocalDateTime());
        sample_form_def.setUpdatedDate(randomLocalDateTime());
        assertNotNull(sample_form_def);

        Map<String, Object> mapObject = formDefinitionDaoRowMapper.mapObject(sample_form_def);
        assertNotNull(mapObject);

        assertEquals(sample_form_def.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(sample_form_def.getName(), mapObject.get(FormDefinitionDaoRowMapper.FormDefColumnType.NAME.getColumnName()));
        //TODO: FIGURE OUT THE WAY TO GET THE FIELDS IN THE FORMS AND TEST IT HERE.

        assertEquals(sample_form_def.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(sample_form_def.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link FormDefinitionDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a FormDefinition object with test values
        FormDefinition sample_form_def = TestDataUtility.formDefWithTestValues();
        sample_form_def.setId(TestDataUtility.randomLong());
        sample_form_def.setCreatedDate(randomLocalDateTime());
        sample_form_def.setUpdatedDate(randomLocalDateTime());
        assertNotNull(sample_form_def);

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(sample_form_def.getId());
        when(resultSet.getString(FormDefinitionDaoRowMapper.FormDefColumnType.NAME.getColumnName())).thenReturn(sample_form_def.getName());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sample_form_def.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sample_form_def.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        FormDefinition verifyUser = formDefinitionDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyUser);

        assertEquals(sample_form_def.getId(), verifyUser.getId());
        assertEquals(sample_form_def.getName(), verifyUser.getName());
        //TODO: FIGURE OUT THE WAY TO GET THE FIELDS IN THE FORMS AND TEST IT HERE.
        assertEquals(sample_form_def.getCreatedDate(), verifyUser.getCreatedDate());
        assertEquals(sample_form_def.getUpdatedDate(), verifyUser.getUpdatedDate());
    }
}
