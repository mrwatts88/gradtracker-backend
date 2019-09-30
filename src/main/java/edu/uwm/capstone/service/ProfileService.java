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

        // TODO make sure user has access to read

        Profile profile = profileDao.read(profileId);

        if (profile == null) {
            throw new ServiceException("Profile with ID: " + profileId + " not found.");
        }
        return profile;
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

        // TODO make sure user has access to update

        if (profileDao.read(profile.getId()) == null) {
            throw new ServiceException("Could not update Profile " + profile.getId() + " - record not found.");
        }
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileDao.update(profile);
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

        // TODO make sure user has access to delete

        if (profileDao.read(profileId) == null) {
            throw new ServiceException("Could not delete Profile " + profileId + " - record not found.");
        }
        return profileDao.delete(profileId);
    }
}
