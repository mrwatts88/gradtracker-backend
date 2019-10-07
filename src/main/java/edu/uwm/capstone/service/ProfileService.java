package edu.uwm.capstone.service;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
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
     * Create a {@link Profile} object.
     *
     * @param profile {@link Profile}
     * @return {@link Profile}
     */
    public Profile create(Profile profile) {
        LOG.trace("Creating profile {}", profile);

        Assert.notNull(profile, "Profile must not be null");
        Assert.isNull(profile.getId(), "Profile ID must be null");
        Assert.notNull(profile.getEmail(),  "Profile email must not be null");
        Assert.notNull(profile.getPassword(),  "Profile password must not be null");

        // TODO check if email is legit
        //  check if email already exists in db,
        //  check if panther id already exits in db

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileDao.create(profile);
    }

    /**
     * Retrieve a {@link Profile} object by its Id.
     *
     * @param profileId
     * @return {@link Profile}
     */
    public Profile read(Long profileId) {
        LOG.trace("Reading profile {}", profileId);

        // TODO make sure user has access to read

        Profile profile = profileDao.read(profileId);

        if (profile == null) {
            throw new ServiceException("Profile with ID: " + profileId + " not found.");
        }
        return profile;
    }

    /**
     * Update the provided {@link Profile} object.
     *
     * @param profile {@link Profile}
     * @return true if successful
     */
    public boolean update(Profile profile) {
        LOG.trace("Updating profile {}", profile);

        Assert.notNull(profile, "Profile must not be null");
        Assert.notNull(profile.getId(), "Profile Id must not be null");
        Assert.notNull(profile.getEmail(),  "Profile email must not be null");
        Assert.notNull(profile.getPassword(),  "Profile password must not be null");

        if (profileDao.read(profile.getId()) == null) {
            throw new ServiceException("Could not update Profile " + profile.getId() + " - record not found.");
        }
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profileDao.update(profile);
    }

    /**
     * Delete a {@link Profile} object by its Id.
     *
     * @param profileId
     * @return true if successful
     */
    public boolean delete(Long profileId) {
        LOG.trace("Deleting profile {}", profileId);

        if (profileDao.read(profileId) == null) {
            throw new ServiceException("Could not delete Profile " + profileId + " - record not found.");
        }
        return profileDao.delete(profileId);
    }
}
