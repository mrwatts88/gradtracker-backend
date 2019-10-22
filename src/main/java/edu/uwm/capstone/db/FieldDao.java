package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Field;
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
public class FieldDao extends BaseDao<Long, Field>{
    private static final Logger LOG = LoggerFactory.getLogger(FieldDao.class);
    @Override
    public Field create(Field field) {
        if (field == null) {
            throw new DaoException("field cannot be null");
        } else if (field.getId() != null) {
            throw new DaoException("When creating a new field, the id should be null, but was set to " + field.getId());
        } else if (field.getFormId() == null) {
            throw new DaoException("When creating a new field, the form id should not be null");
        }

        LOG.trace("Creating field {}", field);
        field.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createField"),
                new MapSqlParameterSource(rowMapper.mapObject(field)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create field %s - affected %s rows", field.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        field.setId(id);
        return field;
    }

    @Override
    public Field read(Long id) {
        LOG.trace("Reading field {}", id);
        try {
            return (Field) this.jdbcTemplate.queryForObject(sql("readField"), new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Field> readFieldsByFormId(Long id) {
        LOG.trace("Reading field with form_id {}", id);
        try {
            return this.jdbcTemplate.query(sql("readFieldsByFormId"), new MapSqlParameterSource("form_id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Field field) {
        if (field == null) {
            throw new DaoException("field cannot be null");
        } else if (field.getId() == null) {
            throw new DaoException("When updating a field, the id should not be null");
        }  else if (field.getFormId() == null) {
            throw new DaoException("When updating a field, the form id should not be null");
        }

        LOG.trace("Updating field {}", field);
        field.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateField"), new MapSqlParameterSource(rowMapper.mapObject(field)));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update field %s - affected %s rows", field.toString(), result));
        }
    }

    @Override
    public void delete(Long id) {
        LOG.trace("Deleting field {}", id);
        int result = this.jdbcTemplate.update(sql("deleteField"), new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete field %s affected %s rows", id, result));
        }
    }

    public void deleteFieldsByFromId(Long id) {
        LOG.trace("Deleting fields with form id {}", id);
        int result = this.jdbcTemplate.update(sql("deleteFieldsByFormId"), new MapSqlParameterSource("form_id", id));
        if (result < 1) {
            throw new DaoException(String.format("Failed attempt to delete fields with form id %s affected %s rows", id, result));
        }
    }
}
