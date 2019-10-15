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
import java.util.List;

public class FormDefinitionDao extends BaseDao<Long, FormDefinition> {
    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionDao.class);

    @Autowired
    private FieldDefinitionDao fieldDefinitionDao;

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
            fd.setFormDefId(formDef.getId());
            fieldDefinitionDao.create(fd);
        }
        return formDef;
    }

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

    public List<FormDefinition> readAll() {
        LOG.trace("Reading all form definitions");
        List<FormDefinition> formDefinitions = this.jdbcTemplate.query(sql("readAllFormDefs"), rowMapper);
        // uncomment this to return form definitions with their field definitions
//        for (FormDefinition fd : formDefinitions) {
//            fd.setFieldDefs(fieldDefinitionDao.readFieldDefsByFormDefId(fd.getId()));
//        }
        return formDefinitions;
    }

    @Override
    public void update(FormDefinition form) {
//        if(form == null)
//        {
//            throw new DaoException("form cannot be null");
//        }
//        else if(form.getId() == null)
//        {
//            throw new DaoException("When creating a new form the id should not be null, it was null");
//        }
//
//        LOG.trace("Updating profile {}", form);
//        form.setUpdatedDate(LocalDateTime.now());
//        int result = this.jdbcTemplate.update(sql("updateform"), new MapSqlParameterSource(rowMapper.mapObject(form)));
//        if (result != 1) {
//            throw new DaoException(String.format("Failed attempt to update form %s - affected %s rows", form.toString(), result));
//        }
    }

    @Override
    public void delete(Long id) {
        LOG.trace("Deleting form definition {}", id);
        fieldDefinitionDao.deleteFieldDefsByFromDefId(id);
        int result = this.jdbcTemplate.update(sql("deleteFormDef"), new MapSqlParameterSource("id", id));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete form %s affected %s rows", id, result));
        }
    }
}
