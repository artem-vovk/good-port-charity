package com.charity.charity.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, BENEFICIARY, ADMIN;

    @Override
    public String getAuthority() {
        return name(); //return Role as string
    }
}
