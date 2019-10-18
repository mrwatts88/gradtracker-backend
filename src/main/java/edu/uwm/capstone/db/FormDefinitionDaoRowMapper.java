package edu.uwm.capstone.db;

import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.FormDefinitionDaoRowMapper.FormDefColumnType.NAME;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class FormDefinitionDaoRowMapper extends BaseRowMapper<FormDefinition> {

    public enum FormDefColumnType {
        NAME();


        private String columnName;

        FormDefColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(FormDefinition object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(NAME.getColumnName(), object.getName());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public FormDefinition mapRow(ResultSet resultSet, int i) throws SQLException {
        FormDefinition formDefinition = new FormDefinition();
        formDefinition.setId(resultSet.getLong(ID.getColumnName()));
        formDefinition.setName(resultSet.getString((NAME.getColumnName())));
        formDefinition.setCreatedDate(dateFromJavaTime(resultSet.getObject(CREATED_DATE.getColumnName())));
        formDefinition.setUpdatedDate(dateFromJavaTime(resultSet.getObject(UPDATED_DATE.getColumnName())));
        return formDefinition;
    }

}
