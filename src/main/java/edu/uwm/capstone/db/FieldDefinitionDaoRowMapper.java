package edu.uwm.capstone.db;

import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.FieldDefinitionDaoRowMapper.FieldDefColumnType.*;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class FieldDefinitionDaoRowMapper extends BaseRowMapper<FieldDefinition> {

    public enum FieldDefColumnType {
        FORM_DEF_ID(),
        LABEL(),
        FIELD_INDEX(),
        INPUT_TYPE(),
        DATA_TYPE();

        private String columnName;

        FieldDefColumnType() {
            columnName = name().toLowerCase();
        }

        public String getColumnName() { return columnName; }
    }

    @Override
    public Map<String, Object> mapObject(FieldDefinition object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(FORM_DEF_ID.getColumnName(), object.getFormDefId());
        map.put(LABEL.getColumnName(), object.getLabel());
        map.put(FIELD_INDEX.getColumnName(), object.getFieldIndex());
        map.put(INPUT_TYPE.getColumnName(), object.getInputType());
        map.put(DATA_TYPE.getColumnName(), object.getDataType());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public FieldDefinition mapRow(ResultSet rs, int rowNum) throws SQLException {
        FieldDefinition folder = new FieldDefinition();
        folder.setId(rs.getLong(ID.getColumnName()));
        folder.setFormDefId(rs.getLong(FORM_DEF_ID.getColumnName()));
        folder.setLabel(rs.getString(LABEL.getColumnName()));
        folder.setFieldIndex(rs.getInt(FIELD_INDEX.getColumnName()));
        folder.setInputType(rs.getString(INPUT_TYPE.getColumnName()));
        folder.setDataType(rs.getString(DATA_TYPE.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(rs.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(rs.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
