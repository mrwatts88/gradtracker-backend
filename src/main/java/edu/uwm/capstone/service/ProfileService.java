package edu.uwm.capstone.service;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.uwm.capstone.sql.exception.DaoException;

@Service("profileService")
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    private ProfileDao profileDao;

    public Profile create(Profile profile) {
        //  check if email is legit (done)
        //  check if email already exists in db, (done)
        // TODO check if password is strong (define strong?)
        if(profile.getEmail().indexOf('@')==-1)
            throw new DaoException("Email provided is not valid");
        if(profile.getEmail().equalsIgnoreCase(profileDao.read(profile.getId()).getEmail()))
            throw new DaoException("Email address already exist in the database");

        // TODO test this service (not here though)
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }

    public Profile update(Profile profile) {

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }

}
