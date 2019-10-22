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
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        assertNotNull(sampleFormDef);
        sampleFormDef.setId(TestDataUtility.randomLong());
        sampleFormDef.setCreatedDate(randomLocalDateTime());
        sampleFormDef.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = formDefinitionDaoRowMapper.mapObject(sampleFormDef);
        assertNotNull(mapObject);

        assertEquals(sampleFormDef.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(sampleFormDef.getName(), mapObject.get(FormDefinitionDaoRowMapper.FormDefColumnType.NAME.getColumnName()));
        assertEquals(sampleFormDef.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(sampleFormDef.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link FormDefinitionDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a FormDefinition object with test values
        FormDefinition sampleFormDef = TestDataUtility.formDefWithTestValues();
        assertNotNull(sampleFormDef);
        sampleFormDef.setId(TestDataUtility.randomLong());
        sampleFormDef.setCreatedDate(randomLocalDateTime());
        sampleFormDef.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(sampleFormDef.getId());
        when(resultSet.getString(FormDefinitionDaoRowMapper.FormDefColumnType.NAME.getColumnName())).thenReturn(sampleFormDef.getName());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleFormDef.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleFormDef.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        FormDefinition verifyFormDef = formDefinitionDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyFormDef);

        assertEquals(sampleFormDef.getId(), verifyFormDef.getId());
        assertEquals(sampleFormDef.getName(), verifyFormDef.getName());
        assertEquals(sampleFormDef.getCreatedDate(), verifyFormDef.getCreatedDate());
        assertEquals(sampleFormDef.getUpdatedDate(), verifyFormDef.getUpdatedDate());
    }
}
