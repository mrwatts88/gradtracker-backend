package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.FieldDefinition;
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
public class FieldDefinitionDaoRowMapperComponentTest {
    @Autowired
    FieldDefinitionDaoRowMapper fieldDefinitionDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(fieldDefinitionDaoRowMapper);
    }

    /**
     * Verify that {@link FieldDefinitionDaoRowMapper.FieldDefColumnType#values()} is working correctly.
     */
    @Test
    public void fieldDefinitionColumnType() {
        for (FieldDefinitionDaoRowMapper.FieldDefColumnType columnType : FieldDefinitionDaoRowMapper.FieldDefColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link FieldDefinitionDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a FieldDefinition object with test values
        FieldDefinition sampleFieldDef = TestDataUtility.fieldDefWithTestValues();
        assertNotNull(sampleFieldDef);
        sampleFieldDef.setId(TestDataUtility.randomLong());
        sampleFieldDef.setCreatedDate(randomLocalDateTime());
        sampleFieldDef.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = fieldDefinitionDaoRowMapper.mapObject(sampleFieldDef);
        assertNotNull(mapObject);

        assertEquals(sampleFieldDef.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(sampleFieldDef.getFormDefId(), mapObject.get(FieldDefinitionDaoRowMapper.FieldDefColumnType.FORM_DEF_ID.getColumnName()));
        assertEquals(sampleFieldDef.getLabel(), mapObject.get(FieldDefinitionDaoRowMapper.FieldDefColumnType.LABEL.getColumnName()));
        assertEquals(sampleFieldDef.getFieldIndex(), mapObject.get(FieldDefinitionDaoRowMapper.FieldDefColumnType.FIELD_INDEX.getColumnName()));
        assertEquals(sampleFieldDef.getInputType(), mapObject.get(FieldDefinitionDaoRowMapper.FieldDefColumnType.INPUT_TYPE.getColumnName()));
        assertEquals(sampleFieldDef.getDataType(), mapObject.get(FieldDefinitionDaoRowMapper.FieldDefColumnType.DATA_TYPE.getColumnName()));
        assertEquals(sampleFieldDef.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(sampleFieldDef.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link FieldDefinitionDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a FieldDefinition object with test values
        FieldDefinition sampleFieldDef = TestDataUtility.fieldDefWithTestValues();
        assertNotNull(sampleFieldDef);
        sampleFieldDef.setId(TestDataUtility.randomLong());
        sampleFieldDef.setCreatedDate(randomLocalDateTime());
        sampleFieldDef.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(sampleFieldDef.getId());
        when(resultSet.getString(FieldDefinitionDaoRowMapper.FieldDefColumnType.LABEL.getColumnName())).thenReturn(sampleFieldDef.getLabel());
        when(resultSet.getInt(FieldDefinitionDaoRowMapper.FieldDefColumnType.FIELD_INDEX.getColumnName())).thenReturn(sampleFieldDef.getFieldIndex());
        when(resultSet.getString(FieldDefinitionDaoRowMapper.FieldDefColumnType.INPUT_TYPE.getColumnName())).thenReturn(sampleFieldDef.getInputType());
        when(resultSet.getString(FieldDefinitionDaoRowMapper.FieldDefColumnType.DATA_TYPE.getColumnName())).thenReturn(sampleFieldDef.getDataType());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleFieldDef.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleFieldDef.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        FieldDefinition verifyFieldDef = fieldDefinitionDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyFieldDef);

        assertEquals(sampleFieldDef.getId(), verifyFieldDef.getId());
        assertEquals(sampleFieldDef.getLabel(), verifyFieldDef.getLabel());
        assertEquals(sampleFieldDef.getFieldIndex(), verifyFieldDef.getFieldIndex());
        assertEquals(sampleFieldDef.getInputType(), verifyFieldDef.getInputType());
        assertEquals(sampleFieldDef.getDataType(), verifyFieldDef.getDataType());
        assertEquals(sampleFieldDef.getCreatedDate(), verifyFieldDef.getCreatedDate());
        assertEquals(sampleFieldDef.getUpdatedDate(), verifyFieldDef.getUpdatedDate());
    }
}
