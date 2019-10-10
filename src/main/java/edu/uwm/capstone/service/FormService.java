package edu.uwm.capstone.service;

import edu.uwm.capstone.db.FormDao;
import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.service.exception.ServiceException;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FormService")
public class FormService {
    private static final Logger LOG = LoggerFactory.getLogger(FormService.class);
    private final FormDao FormDao;
    @Autowired
    public FormService(FormDao FormDao) {
//        this.passwordEncoder = passwordEncoder;
        this.FormDao = FormDao;
    }
    /**
     * TODO finish javaDoc
     *
     * @param form
     * @return
     * @throws ServiceException if form could not be created
     */
    public Form create(Form form) throws ServiceException {
        LOG.trace("Creating profile {}", form);

        try {
            return FormDao.create(form);
        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(String.format("Failed attempt to create form %s", form.toString()));
        }
    }

    /**
     * TODO finish javaDoc
     *
     * @param formId
     * @return
     * @throws ServiceException if form could not be read
     */
    public Form read(Long formId) throws ServiceException {
        LOG.trace("Reading form {}", formId);

        Form Form = FormDao.read(formId);

        if (Form == null) {
            throw new ServiceException("Form with ID: " + formId + " not found.");
        }
        return Form;
    }

    /**
     * TODO finish javaDoc
     *
     * @param form
     * @return
     * @throws ServiceException if form could not be updated
     */
    public boolean update(Form form) throws ServiceException {
        LOG.trace("Updating form {}", form);

        if (FormDao.read(form.getId()) == null) {
            throw new ServiceException("Could not update form " + form.getId() + " - record not found.");
        }
        return FormDao.update(form);
    }

    /**
     * TODO finish javaDoc
     *
     * @param formId
     * @return
     * @throws ServiceException if profile could not be deleted
     */
    public boolean delete(Long formId) throws ServiceException {
        LOG.trace("Deleting profile {}", formId);

        if (FormDao.read(formId) == null) {
            throw new ServiceException("Could not delete form " + formId + " - record not found.");
        }
        return FormDao.delete(formId);
    }
}
