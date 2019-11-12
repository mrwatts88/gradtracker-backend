package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.Role;
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
public class RoleDaoRowMapperComponentTest {
    @Autowired
    RoleDaoRowMapper roleDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(roleDaoRowMapper);
    }

    /**
     * Verify that {@link RoleDaoRowMapper.RoleColumnType#values()} is working correctly.
     */
    @Test
    public void userColumnType() {
        for (RoleDaoRowMapper.RoleColumnType columnType : RoleDaoRowMapper.RoleColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link RoleDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a user object with test values
        Role role = TestDataUtility.roleWithTestValues();
        assertNotNull(role);
        role.setId(TestDataUtility.randomLong());
        role.setCreatedDate(randomLocalDateTime());
        role.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = roleDaoRowMapper.mapObject(role);
        assertNotNull(mapObject);

        assertEquals(role.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(role.getName(), mapObject.get(RoleDaoRowMapper.RoleColumnType.NAME.getColumnName()));
        assertEquals(role.getDescription(), mapObject.get(RoleDaoRowMapper.RoleColumnType.DESCRIPTION.getColumnName()));
        assertEquals(role.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(role.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link RoleDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a user object with test values
        Role role = TestDataUtility.roleWithTestValues();
        assertNotNull(role);
        role.setId(TestDataUtility.randomLong());
        role.setCreatedDate(randomLocalDateTime());
        role.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(role.getId());
        when(resultSet.getString(RoleDaoRowMapper.RoleColumnType.NAME.getColumnName())).thenReturn(role.getName());
        when(resultSet.getString(RoleDaoRowMapper.RoleColumnType.DESCRIPTION.getColumnName())).thenReturn(role.getDescription());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(role.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(role.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        Role verifyRole = roleDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyRole);

        assertEquals(role.getId(), verifyRole.getId());
        assertEquals(role.getName(), verifyRole.getName());
        assertEquals(role.getDescription(), verifyRole.getDescription());
        assertEquals(role.getCreatedDate(), verifyRole.getCreatedDate());
        assertEquals(role.getUpdatedDate(), verifyRole.getUpdatedDate());
    }
}
