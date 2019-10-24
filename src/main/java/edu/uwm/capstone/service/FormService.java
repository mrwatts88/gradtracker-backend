package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FormDao;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service("formService")
public class FormService {

    private static final Logger LOG = LoggerFactory.getLogger(FormService.class);

    private final FormDao formDao;

    @Autowired
    public FormService(FormDao formDao) {
        this.formDao = formDao;
    }

    /**
     * Given a {@link Form} form, returns a fully instantiated Form.
     *
     * @param form Form
     * @return Form
     */
    public Form create(Form form) {
        LOG.trace("Creating form definition {}", form);
        checkValidForm(form, true);
        return formDao.create(form);
    }

    /**
     * Given a form ID, returns the corresponding {@link Form}.
     *
     * @param formId
     * @return Form
     */
    public Form read(Long formId) {
        LOG.trace("Reading form definition {}", formId);

        Form form = formDao.read(formId);

        if (form == null) {
            throw new EntityNotFoundException("Form with ID: " + formId + " not found.");
        }

        return form;
    }

    /**
     * Retrieves all {@link Form} objects.
     *
     * @return
     */
    public List<Form> readAll() {
        return formDao.readAll();
    }

    /**
     * Retrieves all {@link Form} objects with {@link Form#userId} = userId.
     *
     * @return
     */
    public List<Form> readAllByUserId(Long userId) {
        // TODO finish this method
        return Collections.emptyList();
    }

    /**
     * Updates the given {@link Form}.
     *
     * @param form
     * @return
     */
    public void update(Form form) {
        LOG.trace("Updating form {}", form);

        checkValidForm(form, false);
        Form formInDb = formDao.read(form.getId());

        if (formInDb == null) {
            throw new EntityNotFoundException("Could not update form " + form.getId() + " - record not found.");
        }

        HashSet<Long> fieldIdsAssociatedWithOldFormDef = formInDb.getFields().stream().map(Field::getId).collect(Collectors.toCollection(HashSet::new));
        for (Field fd : form) {
            if (fd.getId() != null) {
                Assert.isTrue(fieldIdsAssociatedWithOldFormDef.contains(fd.getId()), "Could update form " + form.getId() +
                        " - found a field with id = " + fd.getId() + " which is not associated with this form");
            }
        }

        formDao.update(form);
    }

    /**
     * Deletes the {@link Form} corresponding to the given formId.
     *
     * @param formId
     * @return
     */
    public void delete(Long formId) {
        LOG.trace("Deleting form definition {}", formId);
        if (formDao.read(formId) == null) {
            throw new EntityNotFoundException("Could not delete form " + formId + " - record not found.");
        }
        formDao.delete(formId);
    }

    /**
     * Checks if the given {@link Form} form is valid.
     * The form's Id and all field Ids are asserted to be null, if checkNullId is true.
     *
     * @param form
     * @param checkNullId
     */
    private void checkValidForm(Form form, boolean checkNullId) {
        Assert.notNull(form, "Form cannot be null");
        if (checkNullId)
            Assert.isNull(form.getId(), "Form id should be null");
        Assert.notNull(form.getFormDefId(), "Form's form definition id should not be null");
        Assert.notNull(form.getUserId(), "Form's user id should not be null");
        // TODO check that user exists
        Assert.notNull(form.getFields(), "Form fields cannot be null");
        Assert.notEmpty(form.getFields(), "Form must have at least one field");

        for (Field fd : form) {
            Assert.notNull(fd, "Field should not be null");
            if (checkNullId)
                Assert.isNull(fd.getId(), "Field id should be null");
            Assert.notNull(fd.getFieldDefId(), "Field's field definition id should not be null");
            // TODO check that field definition exists
            Assert.notNull(fd.getData(), "Field data cannot be null");
        }
    }
}
