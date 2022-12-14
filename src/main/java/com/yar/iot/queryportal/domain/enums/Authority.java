package com.yar.iot.queryportal.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return this.equals(ADMIN) ? "ADMIN" : "USER";
    }
}
