package edu.uwm.capstone.db;

import edu.uwm.capstone.model.UserLoginCredentials;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.UserLoginCredentialsDaoRowMapper.UserLoginCredentialsColumnType.*;

public class UserLoginCredentialsDaoRowMapper extends BaseRowMapper<UserLoginCredentials> {

    public enum UserLoginCredentialsColumnType {
        EMAIL(),
        PASSWORD();

        private String columnName;

        UserLoginCredentialsColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() { return columnName; }
    }

    @Override
    public Map<String, Object> mapObject(UserLoginCredentials object) {
        Map<String, Object> map = new HashMap<>();
        map.put(EMAIL.getColumnName(), object.getEmail());
        map.put(PASSWORD.getColumnName(), object.getPassword());
        return map;
    }

    @Override
    public UserLoginCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserLoginCredentials folder = new UserLoginCredentials();
        folder.setEmail(rs.getString(EMAIL.getColumnName()));
        folder.setPassword(rs.getString(PASSWORD.getColumnName()));
        return folder;
    }
}
