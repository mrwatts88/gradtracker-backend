package edu.uwm.capstone.service;

import edu.uwm.capstone.db.UserLoginCredentialsDao;
import edu.uwm.capstone.model.UserLoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserLoginCredentialsDao userLoginCredentialsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLoginCredentials userLoginCredentials = userLoginCredentialsDao.read(username);
        if (userLoginCredentials == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userLoginCredentials.getEmail(), userLoginCredentials.getPassword(), emptyList());
    }
}