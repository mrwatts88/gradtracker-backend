package edu.uwm.capstone.security;

import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.readByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

//        user.setAuthorities(Collections.singletonList(Authorities.TEST));

        return user;
    }
}