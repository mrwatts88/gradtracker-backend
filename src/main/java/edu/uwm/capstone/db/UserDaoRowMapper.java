package edu.uwm.capstone.db;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.UserDaoRowMapper.UserColumnType.*;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class UserDaoRowMapper extends BaseRowMapper<User> {

    public enum UserColumnType {
        FIRST_NAME(),
        LAST_NAME(),
        PASSWORD(),
        PANTHER_ID(),
        EMAIL(),
        ISACCOUNTNONEXPIRED(),
        ISACCOUNTNONLOCKED(),
        ISCREDENTIALSNONEXPIRED();

        private String columnName;

        UserColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(User object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(FIRST_NAME.getColumnName(), object.getFirstName());
        map.put(LAST_NAME.getColumnName(), object.getLastName());
        map.put(PASSWORD.getColumnName(), object.getPassword());
        map.put(PANTHER_ID.getColumnName(), object.getPantherId());
        map.put(EMAIL.getColumnName(), object.getEmail());
        map.put(ISACCOUNTNONEXPIRED.getColumnName(), object.getIsAccountNonExpired());
        map.put(ISACCOUNTNONLOCKED.getColumnName(), object.getIsAccountNonLocked());
        map.put(ISCREDENTIALSNONEXPIRED.getColumnName(), object.getIsCredentialsNonExpired());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User folder = new User();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setFirstName(rs.getString(FIRST_NAME.getColumnName()));
        folder.setLastName(rs.getString(LAST_NAME.getColumnName()));
        folder.setPassword(rs.getString(PASSWORD.getColumnName()));
        folder.setPantherId(rs.getString(PANTHER_ID.getColumnName()));
        folder.setEmail(rs.getString(EMAIL.getColumnName()));
        folder.setAccountNonExpired(rs.getBoolean(ISACCOUNTNONEXPIRED.getColumnName()));
        folder.setAccountNonLocked(rs.getBoolean(ISACCOUNTNONLOCKED.getColumnName()));
        folder.setCredentialsNonExpired(rs.getBoolean(ISCREDENTIALSNONEXPIRED.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
