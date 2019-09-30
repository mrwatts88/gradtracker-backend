package edu.uwm.capstone.security;

import edu.uwm.capstone.model.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsImpl extends User {

    private Profile profile;

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserDetailsImpl(Profile profile, Collection<? extends GrantedAuthority> authorities) {
        super(profile.getEmail(), (String) profile.getPassword(), authorities);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

}
