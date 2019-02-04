package edu.uwm.capstone.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import edu.uwm.capstone.model.profile.Profile;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.NAME;
import static edu.uwm.capstone.db.ProfileDaoRowMapper.ProfileColumnType.PROJECT;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class ProfileDaoRowMapper extends BaseRowMapper<Profile> {

    public enum ProfileColumnType {
        NAME(),
        PROJECT(),
        ;

        private String columnName;

        ProfileColumnType() {
            columnName = name().toLowerCase();
        }

        ProfileColumnType(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName(){
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(Profile object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(NAME.getColumnName(), object.getName());
        map.put(PROJECT.getColumnName(), object.getProject());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
        Profile folder = new Profile();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setName(rs.getString(NAME.getColumnName()));
        folder.setProject(rs.getString(PROJECT.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }

}
