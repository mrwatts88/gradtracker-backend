package edu.uwm.capstone.service;

import edu.uwm.capstone.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("profileService")
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Profile create(Profile profile) {

        // TODO check if email is legit
        //  check if email already exists in db,
        //  check if password is strong

        // TODO test this service (not here though)

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }

    public Profile update(Profile profile) {

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }

}
