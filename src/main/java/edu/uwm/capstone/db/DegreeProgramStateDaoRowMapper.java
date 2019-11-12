package edu.uwm.capstone.db;
import edu.uwm.capstone.model.DegreeProgramState;
import edu.uwm.capstone.sql.dao.BaseRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static edu.uwm.capstone.db.DegreeProgramStateDaoRowMapper.DegreeProgramStateColumnType.*;
import static edu.uwm.capstone.sql.dao.BaseRowMapper.BaseColumnType.*;

public class DegreeProgramStateDaoRowMapper extends BaseRowMapper<DegreeProgramState> {

    public enum DegreeProgramStateColumnType {
        DEGREE_PROGRAM_ID(),
        NAME(),
        DESCRIPTION();

        private String columnName;
        DegreeProgramStateColumnType() { columnName = name().toLowerCase(); }

        public String getColumnName() { return columnName; }
    }

    @Override
    public Map<String, Object> mapObject(DegreeProgramState object) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID.getColumnName(), object.getId());
        map.put(DEGREE_PROGRAM_ID.getColumnName(), object.getDegreeProgramId());
        map.put(NAME.getColumnName(), object.getName());
        map.put(DESCRIPTION.getColumnName(), object.getDescription());
        map.put(CREATED_DATE.getColumnName(), javaTimeFromDate(object.getCreatedDate()));
        map.put(UPDATED_DATE.getColumnName(), javaTimeFromDate(object.getUpdatedDate()));
        return map;
    }

    @Override
    public DegreeProgramState mapRow(ResultSet resultSet, int i) throws SQLException {
        DegreeProgramState folder = new DegreeProgramState();
        folder.setId(resultSet.getLong(ID.getColumnName()));
        folder.setDegreeProgramId(resultSet.getLong(DEGREE_PROGRAM_ID.getColumnName()));
        folder.setName(resultSet.getString(NAME.getColumnName()));
        folder.setDescription(resultSet.getString(DESCRIPTION.getColumnName()));
        folder.setCreatedDate(dateFromJavaTime(resultSet.getObject(CREATED_DATE.getColumnName())));
        folder.setUpdatedDate(dateFromJavaTime(resultSet.getObject(UPDATED_DATE.getColumnName())));
        return folder;
    }
}
