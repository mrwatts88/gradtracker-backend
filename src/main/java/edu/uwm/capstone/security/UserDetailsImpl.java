package edu.uwm.capstone.security;

import edu.uwm.capstone.model.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Collections.emptyList;

public class UserDetailsImpl implements UserDetails {

    private Profile profile;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a {@link UserDetails} from a {@link Profile} without any authorities.
     *
     * @param profile the {@link Profile}
     */
    UserDetailsImpl(Profile profile) {
        this.profile = profile;
        this.authorities = emptyList();
    }

    /**
     * Constructs a {@link UserDetails} from a {@link Profile} with authorities.
     *
     * @param profile the {@link Profile}
     * @param authorities the list of {@link GrantedAuthority}s
     */
    UserDetailsImpl(Profile profile, Collection<? extends GrantedAuthority> authorities) {
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
        return profile.getPassword();
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
