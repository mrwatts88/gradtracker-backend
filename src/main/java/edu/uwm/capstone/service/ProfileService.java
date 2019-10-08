package edu.uwm.capstone.service;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.service.exception.UserNotFoundException;
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

        checkValidProfile(profile, true);

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
            throw new UserNotFoundException("Profile with ID: " + profileId + " not found.");
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

        checkValidProfile(profile, false);

        if (profileDao.read(profile.getId()) == null) {
            throw new UserNotFoundException("Could not update Profile " + profile.getId() + " - record not found.");
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
            throw new UserNotFoundException("Could not delete Profile " + profileId + " - record not found.");
        }
        return profileDao.delete(profileId);
    }

    private void checkValidProfile(Profile profile, boolean checkId) {
        Assert.notNull(profile, "Profile must not be null");
        if (checkId)
            Assert.isNull(profile.getId(), "Profile ID must be null");
        Assert.notNull(profile.getEmail(),  "Profile email must not be null");
        Assert.notNull(profile.getPassword(),  "Profile password must not be null");
        Assert.notNull(profile.getFirstName(),  "Profile first name must not be null");
        Assert.notNull(profile.getFirstName(),  "Profile last name must not be null");
        Assert.notNull(profile.getFirstName(),  "Profile panther id must not be null");
    }
}
