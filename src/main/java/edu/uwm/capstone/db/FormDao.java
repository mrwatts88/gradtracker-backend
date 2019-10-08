package edu.uwm.capstone.db;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;

@SpringBootApplication
public class FormDao extends BaseDao<Long, Form> {
    private static final Logger LOG = LoggerFactory.getLogger(FormDao.class);


    @Override
    public Form create(Form form) {
        if(form == null)
        {
            throw new DaoException("form cannot be null");
        }
        else if(form.getId() != null)
        {
            throw new DaoException("When creating a new form the id should be null, but was set to " + form.getUser_id());
        }
        LOG.trace("Creating profile {}", form);
        form.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createform"),
                new MapSqlParameterSource(rowMapper.mapObject(form)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create form %s - affected %s rows", form.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        form.setId(id);
        return form;
    }

    @Override
    public Form read(Long id) {
        if(id == null)
        {
            throw new DaoException("Form_id cannot be null");
        }
        LOG.trace("Reading profile {}", id);
        try {
            return (Form) this.jdbcTemplate.queryForObject(sql("readform"), new MapSqlParameterSource("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean update(Form form) {
        if(form == null)
        {
            throw new DaoException("form cannot be null");
        }
        else if(form.getId() == null)
        {
            throw new DaoException("When creating a new form the id should not be null, it was null");
        }

        LOG.trace("Updating profile {}", form);
        form.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateform"), new MapSqlParameterSource(rowMapper.mapObject(form)));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update form %s - affected %s rows", form.toString(), result));
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        if(id == null)
        {
            throw new DaoException("Form_id cannot be null");
        }
        LOG.trace("Deleting profile {}", id);
        int result = this.jdbcTemplate.update(sql("deleteform"), new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete form %s affected %s rows", id, result));
        }
        return true;
    }
}
