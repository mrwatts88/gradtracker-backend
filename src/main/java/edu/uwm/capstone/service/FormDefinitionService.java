package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.model.FieldDefinition;
import edu.uwm.capstone.model.FormDefinition;
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
    private final FieldDefinitionService fieldDefinitionService;

    @Autowired
    public FormDefinitionService(FormDefinitionDao formDefinitionDao, FieldDefinitionService fieldDefinitionService) {
        this.formDefinitionDao = formDefinitionDao;
        this.fieldDefinitionService = fieldDefinitionService;
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
            fieldDefinitionService.create(fd);
        }

        return formDef;
    }

//    /**
//     * TODO finish javaDoc
//     *
//     * @param formDefId
//     * @return
//     */
//    public FormDefinition read(Long formDefId) {
//        LOG.trace("Reading form {}", formDefId);
//
//        FormDefinition formDef = formDefinitionDao.read(formDefId);
//
//        if (formDef == null) {
//            throw new ServiceException("Form with ID: " + formDefId + " not found.");
//        }
//        return formDef;
//    }
//
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
//
//    /**
//     * TODO finish javaDoc
//     *
//     * @param formDefId
//     * @return
//     */
//    public boolean delete(Long formDefId) {
//        LOG.trace("Deleting profile {}", formDefId);
//
//        if (formDefinitionDao.read(formDefId) == null) {
//            throw new ServiceException("Could not delete form " + formDefId + " - record not found.");
//        }
//        return formDefinitionDao.delete(formDefId);
//    }
}
