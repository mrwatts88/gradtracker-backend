package edu.uwm.capstone.service;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.sql.exception.DaoException;
import edu.uwm.capstone.sql.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("profileService")
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    private final PasswordEncoder passwordEncoder;
    private final ProfileDao profileDao;

    @Autowired
    public ProfileService(PasswordEncoder passwordEncoder, ProfileDao profileDao) {
        this.passwordEncoder = passwordEncoder;
        this.profileDao = profileDao;
    }

    /**
     * TODO finish javaDoc
     *
     * @param profile
     * @return
     * @throws ServiceException if profile could not be created
     */
    public Profile create(Profile profile) throws ServiceException {
        LOG.trace("Creating profile {}", profile);

        // validate input
        if (profile == null) {
            throw new ServiceException("Request to create a new Profile received null");
        } else if (profile.getId() != null) {
            throw new ServiceException("When creating a new Profile the id should be null, but was set to " + profile.getId());
        }

        // TODO check if email is legit
        //  check if email already exists in db,
        //  check if panther id already exits in db

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        try {
            return profileDao.create(profile);
        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(String.format("Failed attempt to create profile %s", profile.toString()));
        }
    }

    /**
     * TODO finish javaDoc
     *
     * @param profileId
     * @return
     * @throws ServiceException if profile could not be read
     */
    public Profile read(Long profileId) throws ServiceException {
        LOG.trace("Reading profile {}", profileId);

        if (profileId == null) {
            throw new ServiceException("Request to read a Profile received null Id");
        }

        // TODO make sure user has access to read

        Assert.notNull(profileDao.read(profileId), "Could not read Profile " + profileId + " - record not found.");

        return profileDao.read(profileId);
    }

    /**
     * TODO finish javaDoc
     *
     * @param profile
     * @return
     * @throws ServiceException if profile could not be updated
     */
    public boolean update(Profile profile) throws ServiceException {
        LOG.trace("Updating profile {}", profile);

        // validate input
        if (profile == null) {
            throw new ServiceException("Request to update a Profile received null");
        } else if (profile.getId() == null) {
            throw new ServiceException("When updating a Profile the id should not be null");
        }

        // TODO make sure user has access to update

        Assert.notNull(profileDao.read(profile.getId()), "Could not update Profile " + profile.getId() + " - record not found.");

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        try {
            return profileDao.update(profile);
        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(String.format("Failed attempt to update profile %s", profile.toString()));
        }
    }

    /**
     * TODO finish javaDoc
     *
     * @param profileId
     * @return
     * @throws ServiceException if profile could not be deleted
     */
    public boolean delete(Long profileId) throws ServiceException {
        LOG.trace("Deleting profile {}", profileId);

        // validate input
        if (profileId == null) {
            throw new ServiceException("Request to delete a Profile received null Id");
        }

        // TODO make sure user has access to delete

        Assert.notNull(profileDao.read(profileId), "Could not update Profile " + profileId + " - record not found.");

        try {
            return profileDao.delete(profileId);
        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(String.format("Failed attempt to delete profile %s", profileId));
        }
    }
}
