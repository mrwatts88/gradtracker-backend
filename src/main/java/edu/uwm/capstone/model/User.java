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
@JsonIgnoreProperties({"credentialsNonExpired", "accountNonExpired", "accountNonLocked", "username"})
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
    private Set<String> roleNames;

    @ApiModelProperty(hidden = true)
    private Set<Authorities> authorities;

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
    public boolean isAccountNonExpired() {
        return enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled; // TODO do we want to use enabled for this?
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
