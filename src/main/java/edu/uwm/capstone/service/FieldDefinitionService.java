package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FieldDefinitionDao;
import edu.uwm.capstone.model.FieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fieldDefinitionService")
public class FieldDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(FieldDefinitionService.class);
    private final FieldDefinitionDao fieldDefinitionDao;

    @Autowired
    public FieldDefinitionService(FieldDefinitionDao fieldDefinitionDao) {
        this.fieldDefinitionDao = fieldDefinitionDao;
    }

    /**
     * TODO finish javaDoc
     *
     * @param fieldDef
     * @return
     */
    public FieldDefinition create(FieldDefinition fieldDef) {
        LOG.trace("Creating field definition {}", fieldDef);

        return fieldDefinitionDao.create(fieldDef);
    }

//    /**
//     * TODO finish javaDoc
//     *
//     * @param fieldDefId
//     * @return
//     * @throws ServiceException if field could not be read
//     */
//    public FieldDefinition read(Long fieldDefId) {
//        LOG.trace("Reading field {}", fieldDefId);
//
//        FieldDefinition fieldDef = fieldDefinitionDao.read(fieldDefId);
//
//        if (fieldDef == null) {
//            throw new ServiceException("Field with ID: " + fieldDefId + " not found.");
//        }
//        return fieldDef;
//    }
//
//    /**
//     * TODO finish javaDoc
//     *
//     * @param fieldDef
//     * @return
//     * @throws ServiceException if field could not be updated
//     */
//    public boolean update(FieldDefinition fieldDef) {
//        LOG.trace("Updating field {}", fieldDef);
//
//        if (fieldDefinitionDao.read(fieldDef.getId()) == null) {
//            throw new ServiceException("Could not update field " + fieldDef.getId() + " - record not found.");
//        }
//        return fieldDefinitionDao.update(fieldDef);
//    }
//
//    /**
//     * TODO finish javaDoc
//     *
//     * @param fieldDefId
//     * @return
//     * @throws ServiceException if profile could not be deleted
//     */
//    public boolean delete(Long fieldDefId) {
//        LOG.trace("Deleting profile {}", fieldDefId);
//
//        if (fieldDefinitionDao.read(fieldDefId) == null) {
//            throw new ServiceException("Could not delete field " + fieldDefId + " - record not found.");
//        }
//        return fieldDefinitionDao.delete(fieldDefId);
//    }
}
