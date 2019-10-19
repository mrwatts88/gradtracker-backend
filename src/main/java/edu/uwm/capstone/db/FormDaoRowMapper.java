package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.FormDaoRowMapper.FormColumnType.*;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;
public class FormDaoRowMapper extends BaseRowMapper<Form> {
    public enum FormColumnType {
        FORM_DEF_ID(),
        USER_ID(),
        APPROVED();

        private String columnName;

        FormColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(Form object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(USER_ID.getColumnName(), object.getFormDefId());
        map.put(APPROVED.getColumnName(), object.getApproved());
        map.put(FORM_DEF_ID.getColumnName(), object.getFormDefId());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public Form mapRow(ResultSet rs, int rowNum) throws SQLException {
        Form folder = new Form();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setUserId(rs.getLong(USER_ID.getColumnName()));
        folder.setApproved(rs.getBoolean(APPROVED.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        folder.setFormDefId(rs.getLong(FORM_DEF_ID.getColumnName()));
        return folder;
    }
}
