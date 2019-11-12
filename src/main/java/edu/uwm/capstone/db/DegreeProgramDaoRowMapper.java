package edu.uwm.capstone.db;

import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.DegreeProgramDaoRowMapper.DegreeProgramColumnType.DESCRIPTION;
import static edu.uwm.capstone.db.DegreeProgramDaoRowMapper.DegreeProgramColumnType.NAME;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class DegreeProgramDaoRowMapper extends BaseRowMapper<DegreeProgram> {

    public enum DegreeProgramColumnType {
        NAME(),
        DESCRIPTION();

        private String columnName;
        DegreeProgramColumnType() { columnName = name().toLowerCase(); }

        public String getColumnName() { return columnName; }

    }

    @Override
    public Map<String, Object> mapObject(DegreeProgram object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(NAME.getColumnName(), object.getName());
        map.put(DESCRIPTION.getColumnName(), object.getDescription());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public DegreeProgram mapRow(ResultSet resultSet, int i) throws SQLException {
        DegreeProgram folder = new DegreeProgram();
        folder.setId(resultSet.getLong(ID.getColumnName()));
        folder.setName(resultSet.getString(NAME.getColumnName()));
        folder.setDescription(resultSet.getString(DESCRIPTION.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(resultSet.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(resultSet.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
