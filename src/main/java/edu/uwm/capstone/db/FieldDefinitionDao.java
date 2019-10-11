package edu.uwm.capstone.db;

import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;

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
        LOG.trace("Reading field definition {}", id);
        try {
            return (FieldDefinition) this.jdbcTemplate.queryForObject(sql("readField"), new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<FieldDefinition> readFieldDefsByFormDefId(Long id) {
        LOG.trace("Reading field definitions with form_def_id {}", id);
        try {
            return this.jdbcTemplate.query(sql("readFieldDefsByFormDefId"), new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(FieldDefinition field) {
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
    }

    @Override
    public void delete(Long id) {
        LOG.trace("Deleting profile {}", id);
        int result = this.jdbcTemplate.update(sql("deleteFieldDef"), new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete field %s affected %s rows", id, result));
        }
    }

    public void deleteFieldDefsByFromDefId(Long id, int numberOfFields) {
        LOG.trace("Deleting field definitions with form_def_id {}", id);
        int result = this.jdbcTemplate.update(sql("deleteFieldDefsByFormDefId"), new MapSqlParameterSource("id", id));
        if (result != numberOfFields) {
            throw new DaoException(String.format("Failed attempt to delete fields with form_def_id %s affected %s rows", id, result));
        }
    }
}
