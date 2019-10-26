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

    /**
     * Given an instantiated {@link FieldDefinition} object, creates a field definition.
     *
     * @param fieldDef
     * @return
     */
    @Override
    public FieldDefinition create(FieldDefinition fieldDef) {
        if (fieldDef == null) {
            throw new DaoException("field definition cannot be null");
        } else if (fieldDef.getId() != null) {
            throw new DaoException("When creating a new field definition the id should be null, but was set to " + fieldDef.getId());
        } else if (fieldDef.getFormDefId() == null) {
            throw new DaoException("When creating a new field definition the form definition id should not be null");
        }

        LOG.trace("Creating field definition {}", fieldDef);

        fieldDef.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createFieldDef"),
                new MapSqlParameterSource(rowMapper.mapObject(fieldDef)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create field definition %s - affected %s rows", fieldDef.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        fieldDef.setId(id);
        return fieldDef;
    }

    /**
     * Reads a {@link FieldDefinition} object with the corresponding ID.
     *
     * @param id
     * @return
     */
    @Override
    public FieldDefinition read(Long id) {
        LOG.trace("Reading field definition {}", id);
        try {
            return (FieldDefinition) this.jdbcTemplate.queryForObject(sql("readFieldDef"),
                    new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Reads all {@link FieldDefinition} objects with the given {@link edu.uwm.capstone.model.FormDefinition} ID.
     *
     * @param id
     * @return
     */
    public List<FieldDefinition> readFieldDefsByFormDefId(Long id) {
        LOG.trace("Reading field definitions with form_def_id {}", id);
        try {
            return this.jdbcTemplate.query(sql("readFieldDefsByFormDefId"), new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Reads all {@link FieldDefinition} objects' IDs with the given {@link edu.uwm.capstone.model.FormDefinition} ID.
     *
     * @param id
     * @return
     */
    public List<Long> readFieldDefIdsByFormDefId(Long id) {
        LOG.trace("Reading field definition ids with form_def_id {}", id);
        try {
            return this.jdbcTemplate.queryForList(sql("readFieldDefIdsByFormDefId"), new MapSqlParameterSource("id", id), Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Updates the given {@link FieldDefinition} object.
     *
     * @param fieldDef
     * @return
     */
    @Override
    public FieldDefinition update(FieldDefinition fieldDef) {
        if (fieldDef == null) {
            throw new DaoException("field definition cannot be null");
        } else if (fieldDef.getId() == null) {
            throw new DaoException("When updating a field definition, the id should not be null");
        }

        LOG.trace("Updating field definition {}", fieldDef);

        fieldDef.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateFieldDef"), new MapSqlParameterSource(rowMapper.mapObject(fieldDef)));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update field definition %s - affected %s rows", fieldDef.toString(), result));
        }
        return fieldDef;
    }

    /**
     * Deletes the {@link FieldDefinition} with the given ID.
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        LOG.trace("Deleting field definition {}", id);
        int result = this.jdbcTemplate.update(sql("deleteFieldDef"), new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete field definition %s affected %s rows", id, result));
        }
    }

    /**
     * Deletes all {@link FieldDefinition} objects with the given {@link edu.uwm.capstone.model.FormDefinition} ID.
     *
     * @param id
     */
    public void deleteFieldDefsByFromDefId(Long id) {
        LOG.trace("Deleting field definitions with form definition id {}", id);
        int result = this.jdbcTemplate.update(sql("deleteFieldDefsByFormDefId"), new MapSqlParameterSource("id", id));
        if (result < 1) {
            throw new DaoException(String.format("Failed attempt to delete field definitions with form def id %s affected %s rows", id, result));
        }
    }
}
