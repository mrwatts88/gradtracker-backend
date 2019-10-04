package edu.uwm.capstone.security;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ProfileDao profileDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileDao.readByEmail(username);

        if (profile == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(profile);
    }
}