package edu.uwm.capstone.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"authorities", "credentialsNonExpired", "accountNonExpired", "accountNonLocked", "username"})
public class User extends BaseEntity implements UserDetails {

    @JsonFilter("JWTFilter")
    public static class UserMixIn {
    }

    private String firstName;
    private String lastName;
    private String password;
    private String pantherId;
    private String email;
    private Boolean enabled = true; // TODO need to add this column to users table
    private List<String> roleNames;

    @ApiModelProperty(hidden = true)
    private List<String> authorityNames;

    @ApiModelProperty(hidden = true)
    private List<GrantedAuthority> authorities;

    @ApiModelProperty(hidden = true)
    private boolean credentialsNonExpired;

    @ApiModelProperty(hidden = true)
    private boolean accountNonExpired;

    @ApiModelProperty(hidden = true)
    private boolean accountNonLocked;

    @ApiModelProperty(hidden = true)
    private String username;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
            if (authorityNames != null) {
                for (String authName : authorityNames) {
                    authorities.add(new SimpleGrantedAuthority(authName));
                }
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isAccountNonLocked() {
        return !enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
