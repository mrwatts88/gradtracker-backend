package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.FIRST_NAME;
import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.LAST_NAME;
import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.PANTHER_ID;
import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.EMAIL;

import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class ProfileDaoRowMapper extends BaseRowMapper<Profile> {

    public enum ProfileColumnType {
        FIRST_NAME(),
        LAST_NAME(),
        PANTHER_ID(),
        EMAIL();

        private String columnName;

        ProfileColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() { return columnName; }
    }

    @Override
    public Map<String, Object> mapObject(Profile object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(FIRST_NAME.getColumnName(), object.getFirstName());
        map.put(LAST_NAME.getColumnName(), object.getLastName());
        map.put(PANTHER_ID.getColumnName(), object.getPantherId());
        map.put(EMAIL.getColumnName(), object.getEmail());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile folder = new Profile();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setFirstName(rs.getString(FIRST_NAME.getColumnName()));
        folder.setLastName(rs.getString(LAST_NAME.getColumnName()));
        folder.setPantherId(rs.getString(PANTHER_ID.getColumnName()));
        folder.setEmail(rs.getString(EMAIL.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
