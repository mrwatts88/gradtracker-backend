package edu.uwm.capstone.security;

import com.google.common.collect.Sets;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.model.User;

import java.util.Collections;
import java.util.HashSet;

public class SecurityConstants {

    private SecurityConstants() {
    } // removes sonarqube code smell

    static final String SECRET = "SecretKeyToGenJWTs"; // TODO do not hard code this secret
    static final long EXPIRATION_TIME =   Long.MAX_VALUE;//864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    public static final String AUTHENTICATE_URL = "/auth";

    /* default role information */
    public static Role DEFAULT_ROLE;
    public static Role STUDENT_ROLE;

    static {
        DEFAULT_ROLE = Role.builder()
                .name("Admin")
                .description("The default admin role")
                .authorities(Sets.newHashSet(Authorities.values()))
                .build();

        STUDENT_ROLE = Role.builder()
                .name("Student")
                .description("The default student role.")
                .authorities(new HashSet<>())
                .build();
    }

    /* default user information */
    public static User DEFAULT_USER;

    static {
        DEFAULT_USER = User.builder()
                .firstName("default_first_name")
                .lastName("default_last_name")
                .email("default@uwm.edu")
                .pantherId("123456789")
                .password("password")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roleNames(Collections.singleton(DEFAULT_ROLE.getName()))
                .build();
    }

    public static final String DEFAULT_USER_CREDENTIALS = "{ \"email\": \"" + DEFAULT_USER.getEmail() + "\", \"password\": \"" + DEFAULT_USER.getPassword() + "\" }";

}
