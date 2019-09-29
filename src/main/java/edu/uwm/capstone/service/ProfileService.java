package edu.uwm.capstone.service;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.sql.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.uwm.capstone.sql.exception.DaoException;
import org.springframework.util.Assert;

@Service("profileService")
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileDao profileDao;

    public Profile create(Profile profile) {
        //  check if email is legit (done)
        //  check if email already exists in db, (done)
        // TODO check if password is strong (define strong?)
        if(profile.getEmail().indexOf('@')==-1)
            throw new ServiceException("Email provided is not valid");
//        if(profile.getEmail().equalsIgnoreCase(profileDao.read(profile.getId()).getEmail()))
//            throw new ServiceException("Email address already exist in the database");
        try {
            profileDao.readbyemail(profile.getEmail());
        } catch (DaoException e)
        {
            throw new ServiceException(e.getMessage());
        }
        // TODO test this service (not here though)
//        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        profileDao.create(profile);
        return profile;
    }

    public Profile update(Profile profile) {
        if(profile.getEmail().indexOf('@')==-1)
            throw new DaoException("Email provided is not valid");
//        if(!profile.getEmail().equalsIgnoreCase(profileDao.read(profile.getId()).getEmail()))
//            throw new DaoException("Email address does not exist in the database");
        try {
            profileDao.readbyemail(profile.getEmail());
        } catch (DaoException e)
        {
            throw new ServiceException(e.getMessage());
        }
//        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        try {
            profileDao.read(profile.getId());
        } catch (DaoException e)
        {
            throw new ServiceException(e.getMessage());
        }
        profileDao.update(profile);
        return profile;
    }

    public Profile delete(Long profile_id) {
        try{
            profileDao.delete(profile_id);
        }catch (DaoException e)
        {
            throw new ServiceException(e.getMessage());
        }
        return null;
    }
    public Profile read(Long profile_id) {
        return profileDao.read(profile_id);
    }
}
