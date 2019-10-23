package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.Field;
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
public class FieldDaoRowMapperComponentTest {
    @Autowired
    FieldDaoRowMapper fieldDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(fieldDaoRowMapper);
    }

    /**
     * Verify that {@link FieldDaoRowMapper.FieldColumnType#values()} is working correctly.
     */
    @Test
    public void fieldColumnType() {
        for (FieldDaoRowMapper.FieldColumnType columnType : FieldDaoRowMapper.FieldColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link FieldDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a Field object with test values
        Field sampleField = new Field();
        sampleField.setFormId(TestDataUtility.randomLong());
        sampleField.setFieldDefId(TestDataUtility.randomLong());
        sampleField.setData(TestDataUtility.randomAlphabetic(200));

        sampleField.setId(TestDataUtility.randomLong());
        sampleField.setCreatedDate(randomLocalDateTime());
        sampleField.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = fieldDaoRowMapper.mapObject(sampleField);
        assertNotNull(mapObject);

        assertEquals(sampleField.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(sampleField.getFormId(), mapObject.get(FieldDaoRowMapper.FieldColumnType.FORM_ID.getColumnName()));
        assertEquals(sampleField.getFieldDefId(), mapObject.get(FieldDaoRowMapper.FieldColumnType.FIELD_DEF_ID.getColumnName()));
        assertEquals(sampleField.getData(), mapObject.get(FieldDaoRowMapper.FieldColumnType.DATA.getColumnName()));
        assertEquals(sampleField.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(sampleField.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link FieldDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a Field object with test values
        Field sampleField = new Field();
        sampleField.setFormId(TestDataUtility.randomLong());
        sampleField.setFieldDefId(TestDataUtility.randomLong());
        sampleField.setData(TestDataUtility.randomAlphabetic(200));

        sampleField.setId(TestDataUtility.randomLong());
        sampleField.setCreatedDate(randomLocalDateTime());
        sampleField.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(sampleField.getId());
        when(resultSet.getLong(FieldDaoRowMapper.FieldColumnType.FORM_ID.getColumnName())).thenReturn(sampleField.getFormId());
        when(resultSet.getLong(FieldDaoRowMapper.FieldColumnType.FIELD_DEF_ID.getColumnName())).thenReturn(sampleField.getFieldDefId());
        when(resultSet.getString(FieldDaoRowMapper.FieldColumnType.DATA.getColumnName())).thenReturn(sampleField.getData());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleField.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(sampleField.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        Field verifyField = fieldDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyField);

        assertEquals(sampleField.getId(), verifyField.getId());
        assertEquals(sampleField.getFormId(), verifyField.getFormId());
        assertEquals(sampleField.getFieldDefId(), verifyField.getFieldDefId());
        assertEquals(sampleField.getData(), verifyField.getData());
        assertEquals(sampleField.getCreatedDate(), verifyField.getCreatedDate());
        assertEquals(sampleField.getUpdatedDate(), verifyField.getUpdatedDate());
    }
}
