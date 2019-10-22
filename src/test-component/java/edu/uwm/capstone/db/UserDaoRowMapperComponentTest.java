package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.User;
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
public class UserDaoRowMapperComponentTest {

    @Autowired
    UserDaoRowMapper userDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        assertNotNull(userDaoRowMapper);
    }

    /**
     * Verify that {@link UserDaoRowMapper.UserColumnType#values()} is working correctly.
     */
    @Test
    public void userColumnType() {
        for (UserDaoRowMapper.UserColumnType columnType : UserDaoRowMapper.UserColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link UserDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a user object with test values
        User user = TestDataUtility.userWithTestValues();
        assertNotNull(user);
        user.setId(TestDataUtility.randomLong());
        user.setCreatedDate(randomLocalDateTime());
        user.setUpdatedDate(randomLocalDateTime());

        Map<String, Object> mapObject = userDaoRowMapper.mapObject(user);
        assertNotNull(mapObject);

        assertEquals(user.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(user.getFirstName(), mapObject.get(UserDaoRowMapper.UserColumnType.FIRST_NAME.getColumnName()));
        assertEquals(user.getLastName(), mapObject.get(UserDaoRowMapper.UserColumnType.LAST_NAME.getColumnName()));
        assertEquals(user.getPassword(), mapObject.get(UserDaoRowMapper.UserColumnType.PASSWORD.getColumnName()));
        assertEquals(user.getPantherId(), mapObject.get(UserDaoRowMapper.UserColumnType.PANTHER_ID.getColumnName()));
        assertEquals(user.getEmail(), mapObject.get(UserDaoRowMapper.UserColumnType.EMAIL.getColumnName()));
        assertEquals(user.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(user.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link UserDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a user object with test values
        User user = TestDataUtility.userWithTestValues();
        assertNotNull(user);
        user.setId(TestDataUtility.randomLong());
        user.setCreatedDate(randomLocalDateTime());
        user.setUpdatedDate(randomLocalDateTime());

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(user.getId());
        when(resultSet.getString(UserDaoRowMapper.UserColumnType.FIRST_NAME.getColumnName())).thenReturn(user.getFirstName());
        when(resultSet.getString(UserDaoRowMapper.UserColumnType.LAST_NAME.getColumnName())).thenReturn(user.getLastName());
        when(resultSet.getString(UserDaoRowMapper.UserColumnType.PASSWORD.getColumnName())).thenReturn((String) user.getPassword());
        when(resultSet.getString(UserDaoRowMapper.UserColumnType.PANTHER_ID.getColumnName())).thenReturn(user.getPantherId());
        when(resultSet.getString(UserDaoRowMapper.UserColumnType.EMAIL.getColumnName())).thenReturn(user.getEmail());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(user.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(user.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        User verifyUser = userDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyUser);

        assertEquals(user.getId(), verifyUser.getId());
        assertEquals(user.getFirstName(), verifyUser.getFirstName());
        assertEquals(user.getLastName(), verifyUser.getLastName());
        assertEquals(user.getPassword(), verifyUser.getPassword());
        assertEquals(user.getPantherId(), verifyUser.getPantherId());
        assertEquals(user.getEmail(), verifyUser.getEmail());
        assertEquals(user.getCreatedDate(), verifyUser.getCreatedDate());
        assertEquals(user.getUpdatedDate(), verifyUser.getUpdatedDate());
    }
}
