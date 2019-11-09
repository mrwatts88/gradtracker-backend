package edu.uwm.capstone.security;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {

    CREATE_FORM_DEF, CREATE_USER, CREATE_ROLE,
    READ_FORM_DEFS, READ_USER_FORMS, READ_USERS, READ_DEFS,
    REVISE_FORM_DEF, APPROVE_FORM, UPDATE_USER, UPDATE_ROLE,
    DELETE_ROLE;

    @Override
    public String getAuthority() {
        return toString();
    }

}
