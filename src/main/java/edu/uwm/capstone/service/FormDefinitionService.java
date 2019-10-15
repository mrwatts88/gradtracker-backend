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

import java.util.List;

@Service("formDefinitionService")
public class FormDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionService.class);
    private final FormDefinitionDao formDefinitionDao;

    @Autowired
    public FormDefinitionService(FormDefinitionDao formDefinitionDao) {
        this.formDefinitionDao = formDefinitionDao;
    }

    /**
     * TODO finish javaDoc
     *
     * @param formDef
     * @return
     */
    public FormDefinition create(FormDefinition formDef) {
        LOG.trace("Creating form definition {}", formDef);

        Assert.notNull(formDef.getName(),"Form definition name cannot be null");
        Assert.isNull(formDef.getId(), "Form definition id should be null");
        Assert.notEmpty(formDef.getFieldDefs(), "Form definition must have at least one field definition");

        for (FieldDefinition fd : formDef) {
            Assert.isNull(fd.getId(), "Field definition id should be null");
            Assert.notNull(fd.getLabel(), "Field definition label cannot be null");
            Assert.notNull(fd.getInputType(), "Field definition input type cannot be null");
            Assert.notNull(fd.getDataType(), "Field definition data type cannot be null");
        }

        formDefinitionDao.create(formDef);

        return formDef;
    }

    /**
     * TODO finish javaDoc
     *
     * @param formDefId
     * @return
     */
    public FormDefinition read(Long formDefId) {
        LOG.trace("Reading form definition {}", formDefId);

        FormDefinition formDef = formDefinitionDao.read(formDefId);

        if (formDef == null) {
            throw new EntityNotFoundException("Form definition with ID: " + formDefId + " not found.");
        }

        return formDef;
    }

//    /**
//     * TODO finish javaDoc
//     *
//     * @param formDef
//     * @return
//     */
//    public void update(FormDefinition formDef) {
//        LOG.trace("Updating form {}", formDef);
//
//        if (formDefinitionDao.read(formDef.getId()) == null) {
//            throw new ServiceException("Could not update form " + formDef.getId() + " - record not found.");
//        }
//        return formDefinitionDao.update(formDef);
//    }

    /**
     * TODO finish javaDoc
     *
     * @param formDefId
     * @return
     */
    public void delete(Long formDefId) {
        LOG.trace("Deleting form definition {}", formDefId);
        if (formDefinitionDao.read(formDefId) == null) {
            throw new EntityNotFoundException("Could not delete Form definition " + formDefId + " - record not found.");
        }
        formDefinitionDao.delete(formDefId);
    }

    public List<FormDefinition> readAll() {
        return formDefinitionDao.readAll();
    }
}
