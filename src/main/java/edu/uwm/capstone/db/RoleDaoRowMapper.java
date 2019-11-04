package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.RoleDaoRowMapper.RoleColumnType.DESCRIPTION;
import static edu.uwm.capstone.db.RoleDaoRowMapper.RoleColumnType.NAME;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class RoleDaoRowMapper extends BaseRowMapper<Role> {

    public enum RoleColumnType {
        NAME(),
        DESCRIPTION();

        private String columnName;

        RoleColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(Role object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(NAME.getColumnName(), object.getName());
        map.put(DESCRIPTION.getColumnName(), object.getDescription());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role folder = new Role();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setName(rs.getString(NAME.getColumnName()));
        folder.setDescription(rs.getString(DESCRIPTION.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
