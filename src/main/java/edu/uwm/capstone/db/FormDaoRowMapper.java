package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.FormDaoRowMapper.FormColumnType.USER_ID;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class FormDaoRowMapper extends BaseRowMapper<Form> {

    public enum FormColumnType {

        USER_ID();

        private String columnName;

        FormColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() { return columnName; }
    }

    @Override
    public Map<String, Object> mapObject(Form object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getUser_id());
        map.put(USER_ID.getColumnName(), object.getUser_id());
        map.put(CREATED_DATE.getColumnName(), object.getCreatedDate());
        map.put(UPDATED_DATE.getColumnName(), object.getUpdatedDate());
        return map;
    }

    @Override
    public Form mapRow(ResultSet resultSet, int i) throws SQLException {
        Form form = new Form();
        form.setId(resultSet.getLong(ID.getColumnName()));
        form.setUser_id(resultSet.getLong(USER_ID.getColumnName()));
        form.setCreatedDate(dateFromJavaTime(resultSet.getObject(CREATED_DATE.getColumnName())));
        form.setUpdatedDate(dateFromJavaTime(resultSet.getObject(UPDATED_DATE.getColumnName())));
        return form;
    }

}
