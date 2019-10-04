package edu.uwm.capstone.security;

import edu.uwm.capstone.model.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Collections.emptyList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private Profile profile;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * TODO
     *
     * @param profile
     */
    public UserDetailsImpl(Profile profile) {
        this.profile = profile;
        this.authorities = emptyList();
    }

    /**
     * TODO
     *
     * @param profile
     * @param authorities
     */
    public UserDetailsImpl(Profile profile, Collection<? extends GrantedAuthority> authorities) {
        this.profile = profile;
        this.authorities = authorities;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return (String) profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // TODO
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // TODO
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // TODO
    }

    @Override
    public boolean isEnabled() {
        return true;  // TODO
    }
}
