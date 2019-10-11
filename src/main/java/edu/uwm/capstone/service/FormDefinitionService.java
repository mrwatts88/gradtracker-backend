package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FieldDefinitionDao;
import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import edu.uwm.capstone.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service("formDefinitionService")
public class FormDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionService.class);
    private final FormDefinitionDao formDefinitionDao;
    private final FieldDefinitionDao fieldDefinitionDao;

//    private final FieldDefinitionService fieldDefinitionService; // not sure if we even need this

    @Autowired
    public FormDefinitionService(FormDefinitionDao formDefinitionDao, FieldDefinitionDao fieldDefinitionDao) {
        this.formDefinitionDao = formDefinitionDao;
        this.fieldDefinitionDao = fieldDefinitionDao;
    }

    /**
     * TODO finish javaDoc
     *
     * @param formDef
     * @return
     */
    public FormDefinition create(FormDefinition formDef) {
        LOG.trace("Creating form definition {}", formDef);

        formDefinitionDao.create(formDef);

        for (FieldDefinition fd : formDef) {
            fd.setFormDefId(formDef.getId());
            fieldDefinitionDao.create(fd);
        }

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

        formDef.setFieldDefinitions(fieldDefinitionDao.readFieldDefsByFormDefId(formDefId));

        return formDef;
    }

//    /**
//     * TODO finish javaDoc
//     *
//     * @param formDef
//     * @return
//     */
//    public boolean update(FormDefinition formDef) {
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
        FormDefinition fd = read(formDefId);
        if (fd == null) {
            throw new EntityNotFoundException("Could not delete form definition " + formDefId + " - record not found.");
        }
        int numberOfFields = fieldDefinitionDao.readFieldDefsByFormDefId(formDefId).size();
        fieldDefinitionDao.deleteFieldDefsByFromDefId(formDefId, numberOfFields);
        formDefinitionDao.delete(formDefId);
    }
}
