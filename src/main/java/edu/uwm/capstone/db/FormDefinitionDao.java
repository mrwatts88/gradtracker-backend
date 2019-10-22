package edu.uwm.capstone.db;

import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FormDefinitionDao extends BaseDao<Long, FormDefinition> {
    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionDao.class);

    @Autowired
    private FieldDefinitionDao fieldDefinitionDao;

    /**
     * Given an instantiated {@link FormDefinition}, sets the create date and inserts a new record in the database.
     *
     * @param formDef
     * @return
     */
    @Override
    public FormDefinition create(FormDefinition formDef) {
        if (formDef == null) {
            throw new DaoException("formDef cannot be null");
        } else if (formDef.getId() != null) {
            throw new DaoException("When creating a new form def the id should be null, but was set to " + formDef.getId());
        }
        LOG.trace("Creating form definition {}", formDef);

        formDef.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createFormDef"),
                new MapSqlParameterSource(rowMapper.mapObject(formDef)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create form def %s - affected %s rows", formDef.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        formDef.setId(id);
        for (FieldDefinition fd : formDef) {
            fd.setFormDefId(id);
            fieldDefinitionDao.create(fd);
        }
        return formDef;
    }

    /**
     * Returns a {@link FormDefinition} with the given ID from database.
     *
     * @param id
     * @return
     */
    @Override
    public FormDefinition read(Long id) {
        LOG.trace("Reading form definition {}", id);
        try {
            FormDefinition formDef = (FormDefinition) this.jdbcTemplate.queryForObject(sql("readFormDef"), new MapSqlParameterSource("id", id), rowMapper);
            formDef.setFieldDefs(fieldDefinitionDao.readFieldDefsByFormDefId(id));
            return formDef;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns all {@link FormDefinition} objects from the database.
     *
     * @return List of FormDefinitions
     */
    public List<FormDefinition> readAll() {
        LOG.trace("Reading all form definitions");
        List<FormDefinition> formDefinitions = this.jdbcTemplate.query(sql("readAllFormDefs"), rowMapper);
        // uncomment this to return form definitions with their field definitions
        for (FormDefinition fd : formDefinitions) {
            fd.setFieldDefs(fieldDefinitionDao.readFieldDefsByFormDefId(fd.getId()));
        }
        return formDefinitions;
    }

    /**
     * Given a {@link FormDefinition} formDef, updates the corresponding form definition record in the database.
     *
     * @param formDef
     */
    @Override
    public void update(FormDefinition formDef) {
        if (formDef == null) {
            throw new DaoException("form definition cannot be null");
        } else if (formDef.getId() == null) {
            throw new DaoException("When updating a form definition the id should not be null");
        }

        LOG.trace("Updating form definition {}", formDef);
        formDef.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateFormDef"), new MapSqlParameterSource(rowMapper.mapObject(formDef)));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update form definition %s - affected %s rows", formDef.toString(), result));
        }

        // TODO we may want to create an sql query to select all field def ids
        HashSet<Long> fieldDefIdsAssociatedWithOldFormDef = fieldDefinitionDao.readFieldDefsByFormDefId(formDef.getId()).stream().map(FieldDefinition::getId).collect(Collectors.toCollection(HashSet::new));

        Long fieldDefId;
        for (FieldDefinition fd : formDef) {
            fieldDefId = fd.getId();
            if (fieldDefId == null) {                                              // create new field def if has null id
                fd.setFormDefId(formDef.getId());
                fieldDefinitionDao.create(fd);
            } else if (fieldDefIdsAssociatedWithOldFormDef.contains(fieldDefId)) { // update field def if it's connected to formDef
                fieldDefIdsAssociatedWithOldFormDef.remove(fieldDefId);
                fieldDefinitionDao.update(fd);
            }
        }

        fieldDefIdsAssociatedWithOldFormDef.forEach(fdId -> fieldDefinitionDao.delete(fdId)); // remove old field defs
    }

    /**
     * Deletes the {@link FormDefinition} record in the database with the given ID.
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        LOG.trace("Deleting form definition {}", id);
        fieldDefinitionDao.deleteFieldDefsByFromDefId(id);
        int result = this.jdbcTemplate.update(sql("deleteFormDef"), new MapSqlParameterSource("id", id));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete form definition %s affected %s rows", id, result));
        }
    }
}
