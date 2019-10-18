package edu.uwm.capstone.security;

import edu.uwm.capstone.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Collections.emptyList;

public class UserDetailsImpl implements UserDetails {

    private transient User user;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a {@link UserDetails} from a {@link User} without any authorities.
     *
     * @param user the {@link User}
     */
    UserDetailsImpl(User user) {
        this.user = user;
        this.authorities = emptyList();
    }

    /**
     * Constructs a {@link UserDetails} from a {@link User} with authorities.
     *
     * @param user        the {@link User}
     * @param authorities the list of {@link GrantedAuthority}s
     */
    UserDetailsImpl(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // TODO need to know if a user account is expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // TODO need to know if a user account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // TODO need to know if a user's credentials are expired
    }

    @Override
    public boolean isEnabled() {
        return true;  // TODO need to know if a user account is enabled
    }
}
