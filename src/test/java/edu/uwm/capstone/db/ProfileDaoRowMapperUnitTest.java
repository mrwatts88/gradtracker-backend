package edu.uwm.capstone.db;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.profile.Profile;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.util.TestDataUtility;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.dateFromJavaTime;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.javaTimeFromDate;
import static edu.uwm.capstone.util.TestDataUtility.randomLocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class ProfileDaoRowMapperUnitTest {

    @Autowired
    ProfileDaoRowMapper profileDaoRowMapper;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws IOException {
        assertNotNull(profileDaoRowMapper);
    }

    /**
     * Verify that {@link ProfileDaoRowMapper.ProfileColumnType#values()} is working correctly.
     */
    @Test
    public void profileColumnType() {
        for (ProfileDaoRowMapper.ProfileColumnType columnType : ProfileDaoRowMapper.ProfileColumnType.values()) {
            assertNotNull(columnType.name());
            assertNotNull(columnType.getColumnName());
        }
    }

    /**
     * Verify that {@link ProfileDaoRowMapper#mapObject} is working correctly.
     */
    @Test
    public void mapObject() {
        // generate a profile object with test values
        Profile profile = TestDataUtility.profileWithTestValues();
        profile.setId(TestDataUtility.randomLong());
        profile.setCreatedDate(randomLocalDateTime());
        profile.setUpdatedDate(randomLocalDateTime());
        assertNotNull(profile);

        Map<String, Object> mapObject = profileDaoRowMapper.mapObject(profile);
        assertNotNull(mapObject);

        assertEquals(profile.getId(), mapObject.get(BaseRowMapper.BaseColumnType.ID.getColumnName()));
        assertEquals(profile.getName(), mapObject.get(ProfileDaoRowMapper.ProfileColumnType.NAME.getColumnName()));
        assertEquals(profile.getProject(), mapObject.get(ProfileDaoRowMapper.ProfileColumnType.PROJECT.getColumnName()));
        assertEquals(profile.getCreatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())));
        assertEquals(profile.getUpdatedDate(), dateFromJavaTime(mapObject.get(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())));
    }

    /**
     * Verify that {@link ProfileDaoRowMapper#mapRow} is working correctly.
     */
    @Test
    public void mapRow() throws SQLException {
        // generate a profile object with test values
        Profile profile = TestDataUtility.profileWithTestValues();
        profile.setId(TestDataUtility.randomLong());
        profile.setCreatedDate(randomLocalDateTime());
        profile.setUpdatedDate(randomLocalDateTime());
        assertNotNull(profile);

        // define the behavior of the resultSet that is being mocked
        when(resultSet.getLong(BaseRowMapper.BaseColumnType.ID.getColumnName())).thenReturn(profile.getId());
        when(resultSet.getString(ProfileDaoRowMapper.ProfileColumnType.NAME.getColumnName())).thenReturn(profile.getName());
        when(resultSet.getString(ProfileDaoRowMapper.ProfileColumnType.PROJECT.getColumnName())).thenReturn(profile.getProject());
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.CREATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(profile.getCreatedDate()));
        when(resultSet.getObject(BaseRowMapper.BaseColumnType.UPDATED_DATE.getColumnName())).thenReturn(javaTimeFromDate(profile.getUpdatedDate()));

        // exercise the mapRow functionality and verify the expected results
        Profile verifyProfile = profileDaoRowMapper.mapRow(resultSet, 0);
        assertNotNull(verifyProfile);

        assertEquals(profile.getId(), verifyProfile.getId());
        assertEquals(profile.getName(), verifyProfile.getName());
        assertEquals(profile.getProject(), verifyProfile.getProject());
        assertEquals(profile.getCreatedDate(), verifyProfile.getCreatedDate());
        assertEquals(profile.getUpdatedDate(), verifyProfile.getUpdatedDate());
    }

}
