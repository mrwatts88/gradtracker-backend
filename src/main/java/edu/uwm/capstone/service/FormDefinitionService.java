package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.model.FieldDefinition;
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

@Service("formDefinitionService")
public class FormDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionService.class);
    private final FormDefinitionDao formDefinitionDao;

    @Autowired
    public FormDefinitionService(FormDefinitionDao formDefinitionDao) {
        this.formDefinitionDao = formDefinitionDao;
    }

    /**
     * Given a {@link FormDefinition} formDef, returns a fully instantiated FormDefinition
     *
     * @param formDef
     * @return FormDefinition
     */
    public FormDefinition create(FormDefinition formDef) {
        LOG.trace("Creating form definition {}", formDef);
        checkValidFormDefinition(formDef, true);
        return formDefinitionDao.create(formDef);
    }

    /**
     * Given a form definition ID, returns the corresponding {@link FormDefinition}.
     *
     * @param formDefId
     * @return FormDefinition
     */
    public FormDefinition read(Long formDefId) {
        LOG.trace("Reading form definition {}", formDefId);

        FormDefinition formDef = formDefinitionDao.read(formDefId);

        if (formDef == null) {
            throw new EntityNotFoundException("Form definition with ID: " + formDefId + " not found.");
        }

        return formDef;
    }

    /**
     * Updates the given {@link FormDefinition}, formDef.
     *
     * @param formDef
     * @return
     */
    public void update(FormDefinition formDef) {
        LOG.trace("Updating form {}", formDef);

        checkValidFormDefinition(formDef, false);
        FormDefinition formDefinitionInDb = formDefinitionDao.read(formDef.getId());

        if (formDefinitionInDb == null) {
            throw new EntityNotFoundException("Could not update form definition " + formDef.getId() + " - record not found.");
        }

        HashSet<Long> fieldDefIdsAssociatedWithOldFormDef = formDefinitionInDb.getFieldDefs().stream().map(FieldDefinition::getId).collect(Collectors.toCollection(HashSet::new));
        for (FieldDefinition fd : formDef) {
            if (fd.getId() != null) {
                Assert.isTrue(fieldDefIdsAssociatedWithOldFormDef.contains(fd.getId()), "Could update form definition " + formDef.getId() +
                        " - found a field definition with id = " + fd.getId() + " which is not associated with this form definition");
            }
        }

        formDefinitionDao.update(formDef);
    }

    /**
     * Deletes the {@link FormDefinition} corresponding to the given formDefId.
     *
     * @param formDefId
     * @return
     */
    public void delete(Long formDefId) {
        LOG.trace("Deleting form definition {}", formDefId);
        if (formDefinitionDao.read(formDefId) == null) {
            throw new EntityNotFoundException("Could not delete form definition " + formDefId + " - record not found.");
        }
        formDefinitionDao.delete(formDefId);
    }

    /**
     * Reads all {@link FormDefinition} objects.
     *
     * @return
     */
    public List<FormDefinition> readAll() {
        return formDefinitionDao.readAll();
    }

    /**
     * Checks if the given {@link FormDefinition} formDef is valid.
     * The formDefId is asserted to be null, if checkNullId is true.
     *
     * @param formDef
     * @param checkNullId
     */
    private void checkValidFormDefinition(FormDefinition formDef, boolean checkNullId) {
        Assert.notNull(formDef, "Form definition cannot be null");
        if (checkNullId)
            Assert.isNull(formDef.getId(), "Form definition id should be null");
        Assert.notNull(formDef.getName(), "Form definition name cannot be null");
        Assert.notNull(formDef.getFieldDefs(), "Form definition's field definitions cannot be null");
        Assert.notEmpty(formDef.getFieldDefs(), "Form definition must have at least one field definition");

        for (FieldDefinition fd : formDef) {
            Assert.notNull(fd, "Field definition should not be null");
            if (checkNullId)
                Assert.isNull(fd.getId(), "Field definition id should be null");
            Assert.notNull(fd.getLabel(), "Field definition label cannot be null");
            Assert.notNull(fd.getInputType(), "Field definition input type cannot be null");
            Assert.notNull(fd.getDataType(), "Field definition data type cannot be null");
        }
    }
}
