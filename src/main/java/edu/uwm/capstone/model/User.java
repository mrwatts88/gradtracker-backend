package edu.uwm.capstone.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.uwm.capstone.security.Authorities;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"username"}, ignoreUnknown = true)
public class User extends BaseEntity implements UserDetails {

    @JsonFilter("JWTFilter")
    // filters out properties for serialization except for "firstName", "lastName", "pantherId", "email", "enabled", "roleNames", "authorities"
    public static class UserJWTMixIn {
    }

    private String firstName;
    private String lastName;
    private String password;
    private String pantherId;
    private String email;
    private Set<String> roleNames;

    @ApiModelProperty(hidden = true)
    private Set<Authorities> authorities;

    private Boolean enabled = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;

    @ApiModelProperty(hidden = true)
    private String username;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
