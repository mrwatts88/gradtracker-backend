package edu.uwm.capstone.db;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FormDao extends BaseDao<Long, Form>{
    private static final Logger LOG = LoggerFactory.getLogger(FormDao.class);

    @Autowired
    private FieldDao fieldDao;

    @Override
    public Form create(Form form) {
        if (form == null) {
            throw new DaoException("form cannot be null");
        } else if (form.getId() != null) {
            throw new DaoException("When creating a new form, the id should be null, but was set to " + form.getId());
        } else if (form.getFormDefId() == null) {
            throw new DaoException("When creating a new form, the form definition id should not be null");
        } else if (form.getUserId() == null) {
            throw new DaoException("When creating a new form, the user id should not be null");
        }

        LOG.trace("Creating form {}", form);

        form.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createForm"),
                new MapSqlParameterSource(rowMapper.mapObject(form)), keyHolder,
                new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create form %s - affected %s rows", form.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        form.setId(id);
        try {                       // TODO there may be a better way to do this
            for (Field fd : form) {
                fd.setFormId(id);
                fieldDao.create(fd);
            }
        } catch (DaoException | DataIntegrityViolationException e) {
            // need to remove the form if creating any of the fields fails
            delete(id);
            throw e;
        }
        return form;
    }

    @Override
    public Form read(Long id) {
        LOG.trace("Reading form {}", id);
        try {
            Form form = (Form) this.jdbcTemplate.queryForObject(sql("readForm"), new MapSqlParameterSource("id", id), rowMapper);
            form.setFields(fieldDao.readFieldsByFormId(id));
            return form;
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return null;
        }
    }

    public List<Form> readAll() {
        LOG.trace("Reading all form");
        List<Form> forms = this.jdbcTemplate.query(sql("readAllForms"), rowMapper);
        // uncomment this to return forms with their fields
        for (Form fd : forms) {
            fd.setFields(fieldDao.readFieldsByFormId(fd.getId()));
        }
        return forms;
    }

    public List<Form> readAllByUserId(Long userId) {
        LOG.trace("Reading all forms by user id {}", userId);
        List<Form> forms = this.jdbcTemplate.query(sql("readAllFormsByUserId"), new MapSqlParameterSource("user_id", userId), rowMapper);
        // uncomment this to return forms with their fields
        for (Form fd : forms) {
            fd.setFields(fieldDao.readFieldsByFormId(fd.getId()));
        }
        return forms;
    }

    public List<Form> readAllByFormDefId(Long formDefId) {
        LOG.trace("Reading all forms by form definition id {}", formDefId);
        List<Form> forms = this.jdbcTemplate.query(sql("readAllFormsByFormDefId"), new MapSqlParameterSource("form_def_id", formDefId), rowMapper);
        // uncomment this to return forms with their fields
        for (Form fd : forms) {
            fd.setFields(fieldDao.readFieldsByFormId(fd.getId()));
        }
        return forms;
    }

    public List<Form> readAllByPantherId(String pantherId) {
        LOG.trace("Reading all forms by user's Panther ID.");
        List<Form> forms = this.jdbcTemplate.query(sql("readAllFormsByPantherId"), new MapSqlParameterSource("panther_id", pantherId), rowMapper);
        for (Form fd : forms) {
            fd.setFields(fieldDao.readFieldsByFormId(fd.getId()));
        }
        return forms;
    }

    @Override
    public Form update(Form form) {
        if (form == null) {
            throw new DaoException("form cannot be null");
        } else if (form.getId() == null) {
            throw new DaoException("When updating a form the id should not be null");
        }

        LOG.trace("Updating form {}", form);

        form.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateForm"), new MapSqlParameterSource(rowMapper.mapObject(form)));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update form %s - affected %s rows", form.toString(), result));
        }

        HashSet<Long> fieldIdsAssociatedWithOldForm = fieldDao.readFieldsByFormId(form.getId()).stream().map(Field::getId).collect(Collectors.toCollection(HashSet::new));

        Long fieldId;
        for (Field f : form) {
            fieldId = f.getId();
            if (fieldId == null) {                                              // create new field if has null id
                f.setFormId(form.getId());
                fieldDao.create(f);
            } else if (fieldIdsAssociatedWithOldForm.contains(fieldId)) {       // update field if it's connected to form
                fieldIdsAssociatedWithOldForm.remove(fieldId);
                fieldDao.update(f);
            }
        }

        // TODO may need to catch and handle when field update fails (see create method)
        //  or we may want to prevent a field update failure...
        //  this would normally be done in the service layer, but idk if we should do it here too

        fieldIdsAssociatedWithOldForm.forEach(fId -> fieldDao.delete(fId));
        return form;
    }

    @Override
    public void delete(Long id) {
        LOG.trace("Deleting form {}", id);
        fieldDao.deleteFieldsByFromId(id);
        int result = this.jdbcTemplate.update(sql("deleteForm"), new MapSqlParameterSource("id", id));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete form %s affected %s rows", id, result));
        }
    }

    public Form approvalBehavior(Long id, boolean isApproved){
        LOG.trace(isApproved ? "Approving" : "Rejecting" + "form {} in dao", id);
        Form thisForm = read(id);
        thisForm.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateApproval"), new MapSqlParameterSource("id", id).
                addValue("approved", isApproved));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete form %s affected %s rows", id, result));
        }

        return thisForm;
    }
}
