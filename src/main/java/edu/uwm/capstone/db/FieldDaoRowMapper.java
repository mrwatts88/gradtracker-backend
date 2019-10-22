package edu.uwm.capstone.db;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.FieldDaoRowMapper.FieldColumnType.*;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class FieldDaoRowMapper extends BaseRowMapper<Field> {
    public enum FieldColumnType {
        FORM_ID(),
        FIELD_DEF_ID(),
        DATA();

        private String columnName;

        FieldColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    @Override
    public Map<String, Object> mapObject(Field object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(FORM_ID.getColumnName(), object.getFormId());
        map.put(DATA.getColumnName(), object.getData());
        map.put(FIELD_DEF_ID.getColumnName(), object.getFieldDefId());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public Field mapRow(ResultSet rs, int rowNum) throws SQLException {
        Field folder = new Field();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setFormId(rs.getLong(FORM_ID.getColumnName()));
        folder.setData(rs.getString(DATA.getColumnName()));
        folder.setFieldDefId(rs.getLong(FIELD_DEF_ID.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
