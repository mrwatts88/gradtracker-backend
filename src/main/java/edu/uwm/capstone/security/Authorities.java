package edu.uwm.capstone.security;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {

    CREATE_FORM_DEF,    // can use @PreAuthorize
    CREATE_USER,        // can use @PreAuthorize
    CREATE_ROLE,        // can use @PreAuthorize
    READ_ALL_FORMS,     // cannot use @PreAuthorize because user's without this auth can still read their forms
    READ_ALL_USERS,     // cannot use @PreAuthorize because user's without this auth can still read their user
    UPDATE_FORM_DEF,    // can use @PreAuthorize
    APPROVE_FORM,       // can use @PreAuthorize
    UPDATE_USER,        // can use @PreAuthorize
    UPDATE_ROLE,        // can use @PreAuthorize
    DELETE_FORM_DEF,    // can use @PreAuthorize
    DELETE_USER,        // can use @PreAuthorize
    DELETE_ROLE;        // can use @PreAuthorize

    @Override
    public String getAuthority() {
        return toString();
    }

}
