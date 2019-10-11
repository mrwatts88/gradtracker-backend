package edu.uwm.capstone.db;

import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;

public class FieldDefinitionDao extends BaseDao<Long, FieldDefinition> {
    private static final Logger LOG = LoggerFactory.getLogger(FieldDefinitionDao.class);

    @Override
    public FieldDefinition create(FieldDefinition fieldDef) {
        if (fieldDef == null) {
            throw new DaoException("fieldDef cannot be null");
        } else if (fieldDef.getId() != null) {
            throw new DaoException("When creating a new field def the id should be null, but was set to " + fieldDef.getId());
        }
        LOG.trace("Creating field definition {}", fieldDef);

        fieldDef.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createFieldDef"),
                new MapSqlParameterSource(rowMapper.mapObject(fieldDef)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create field def %s - affected %s rows", fieldDef.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        fieldDef.setId(id);
        return fieldDef;
    }

    @Override
    public FieldDefinition read(Long id) {
//        if(id == null)
//        {
//            throw new DaoException("FieldDefinition_id cannot be null");
//        }
//        LOG.trace("Reading profile {}", id);
//        try {
//            return (FieldDefinition) this.jdbcTemplate.queryForObject(sql("readfield"), new MapSqlParameterSource("id", id), rowMapper);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
        return null;
    }

    @Override
    public boolean update(FieldDefinition field) {
//        if(field == null)
//        {
//            throw new DaoException("field cannot be null");
//        }
//        else if(field.getId() == null)
//        {
//            throw new DaoException("When creating a new field the id should not be null, it was null");
//        }
//
//        LOG.trace("Updating profile {}", field);
//        field.setUpdatedDate(LocalDateTime.now());
//        int result = this.jdbcTemplate.update(sql("updatefield"), new MapSqlParameterSource(rowMapper.mapObject(field)));
//        if (result != 1) {
//            throw new DaoException(String.format("Failed attempt to update field %s - affected %s rows", field.toString(), result));
//        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
//        if(id == null)
//        {
//            throw new DaoException("FieldDefinition_id cannot be null");
//        }
//        LOG.trace("Deleting profile {}", id);
//        int result = this.jdbcTemplate.update(sql("deletefield"), new MapSqlParameterSource("id", id));
//        if (result != 1) {
//            throw new DaoException(String.format("Failed attempt to delete field %s affected %s rows", id, result));
//        }
        return true;
    }
}
