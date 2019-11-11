package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FormDao;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.Field;
import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service("formService")
public class FormService {

    private static final Logger LOG = LoggerFactory.getLogger(FormService.class);

    private final FormDao formDao;
    private final FormDefinitionDao formDefinitionDao;
    private final UserDao userDao;

    @Autowired
    public FormService(FormDao formDao, UserDao userDao, FormDefinitionDao formDefinitionDao) {
        this.formDao = formDao;
        this.formDefinitionDao = formDefinitionDao;
        this.userDao = userDao;
    }

    /**
     * Given a {@link Form} form, returns a fully instantiated Form.
     *
     * @param form Form
     * @return Form
     */
    public Form create(Form form) {
        LOG.trace("Creating form definition {}", form);
        FormDefinition formDefinitionInDb = formDefinitionDao.read(form.getFormDefId());

        checkValidForm(form, true, formDefinitionInDb);

        form.setName(formDefinitionInDb.getName());
        form.setApproved(null);

        FieldDefinition fd;
        for (Field f : form) {
            fd = formDefinitionInDb.getFieldDefinitionById(f.getFieldDefId());
            f.setLabel(fd.getLabel());
            f.setFieldIndex(fd.getFieldIndex());
        }

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
     * Retrieves all {@link Form} objects.
     *
     * @return
     */
    public List<Form> readAllByFormDefId(Long formDefId) {
        return formDao.readAllByFormDefId(formDefId);
    }

    /**
     * Retrieves all {@link Form} objects with {@link Form#userId} = userId.
     *
     * @return
     */
    public List<Form> readAllByUserId(Long userId) {
        return formDao.readAllByUserId(userId);
    }

    /**
     * Retrieves all {@link Form} objects associated with the user whose {@link Form#pantherId} = pantherId.
     * @param pantherId
     * @return
     */
    public List<Form> readAllByPantherId(String pantherId) {
        return formDao.readAllByPantherId(pantherId);
    }

    /**
     * Updates the given {@link Form}.
     *
     * @param fields
     * @return
     */
    public Form updateFormFields(Long formId, List<Field> fields) {
        LOG.trace("Updating form {}", formId);

        Form formInDb = formDao.read(formId);

        if (formInDb == null) {
            throw new EntityNotFoundException("Could not update form " + formId + " - record not found.");
        }

        HashSet<Long> fieldIdsAssociatedWithOldForm = formInDb.getFields().stream().map(Field::getId).collect(Collectors.toCollection(HashSet::new));
        for (Field fd : fields) {
            if (fd.getId() != null) {
                Assert.isTrue(fieldIdsAssociatedWithOldForm.contains(fd.getId()), "Could not update form " + formId +
                        " - found a field with id = " + fd.getId() + " which is not associated with this form");
            }
        }

        formInDb.setFields(fields);
        formInDb.setApproved(null);

        FormDefinition formDefinitionInDb = formDefinitionDao.read(formInDb.getFormDefId());
        checkValidForm(formInDb, false, formDefinitionInDb);

        formInDb.setName(formDefinitionInDb.getName());

        FieldDefinition fd;
        for (Field f : formInDb) {
            fd = formDefinitionInDb.getFieldDefinitionById(f.getFieldDefId());
            f.setLabel(fd.getLabel());
            f.setFieldIndex(fd.getFieldIndex());
        }

        formInDb.setCreatedDate(formInDb.getCreatedDate());
        return formDao.update(formInDb);
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

    public Form approve(Long formId, boolean isApproved) {
        LOG.trace(isApproved ? "Approving" : "Rejecting" + "form {}", formId);
        if (formDao.read(formId) == null) {
            throw new EntityNotFoundException("Could not change approval for form " + formId + " - record not found.");
        }

        return formDao.approve(formId, isApproved);
    }

    /**
     * Checks if the given {@link Form} form is valid.
     * The form's Id and all field Ids are asserted to be null, if checkNullId is true.
     *
     * @param form
     * @param checkNullId
     */
    private void checkValidForm(Form form, boolean checkNullId, FormDefinition formDefinitionInDb) {
        Assert.notNull(form, "Form cannot be null");
        if (checkNullId)
            Assert.isNull(form.getId(), "Form id should be null");
        Assert.notNull(form.getFormDefId(), "Form's form definition id should not be null");

        Assert.notNull(formDefinitionInDb, "Form's form definition should exist");

        Assert.isTrue(formDefinitionInDb.getFieldDefs().size() == form.getFields().size(),
                "Form should have same number of fields as its form definition");

        Assert.notNull(form.getUserId(), "Form's user id should not be null");
        Assert.notNull(userDao.read(form.getUserId()), "Form's user should exist");

        Assert.notNull(form.getFields(), "Form fields cannot be null");
        Assert.notEmpty(form.getFields(), "Form must have at least one field");

        HashSet<Long> fieldDefIdsInDb = formDefinitionInDb.getFieldDefs().stream().map(FieldDefinition::getId)
                .collect(Collectors.toCollection(HashSet::new));

        HashSet<Long> seenFieldDefIds = new HashSet<>();
        for (Field fd : form) {
            Assert.notNull(fd, "Field should not be null");

            if (checkNullId)
                Assert.isNull(fd.getId(), "Field id should be null");

            Assert.notNull(fd.getFieldDefId(), "Field's field definition id should not be null");
            Assert.isTrue(fieldDefIdsInDb.contains(fd.getFieldDefId()),
                    "Field's field definition should be apart of the form's definition");
            Assert.notNull(fd.getData(), "Field data cannot be null");

            Assert.isTrue(!seenFieldDefIds.contains(fd.getFieldDefId()), "Field defs must be unique");
            seenFieldDefIds.add(fd.getFieldDefId());
        }
    }
}
