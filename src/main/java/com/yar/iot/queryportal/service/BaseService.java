package com.yar.iot.queryportal.service;


import com.yar.iot.queryportal.domain.enums.Authority;
import com.yar.iot.queryportal.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.yar.iot.queryportal.exception.UserErrorMessage.NOT_AUTHORIZATION;

/**
 * The base service
 * */
@NoRepositoryBean
public class BaseService {

    /**
     * Get current authenticated user
     *
     * @return {@link Authentication}
     * */
    public Authentication getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new BusinessException(NOT_AUTHORIZATION));
    }

    /**
     * Validate current authenticated user is admin
     *
     * @return a boolean for being admin or not
     * */
    public boolean isCurrentUserAdmin(){
        Authentication authentication = getCurrentUser();
        return authentication.getAuthorities().contains(Authority.ADMIN);
    }

}
