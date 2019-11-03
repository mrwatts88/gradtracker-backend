package edu.uwm.capstone.security;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {

    TEST, TEST2; // TODO create authorities

    @Override
    public String getAuthority() {
        return toString();
    }

}
