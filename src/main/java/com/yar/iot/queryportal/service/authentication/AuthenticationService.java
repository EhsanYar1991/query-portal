package com.yar.iot.queryportal.service.authentication;

import com.yar.iot.queryportal.common.request.authentication.AuthenticationRequest;
import com.yar.iot.queryportal.common.request.user.RegisterRequest;
import com.yar.iot.queryportal.common.request.user.UserRequest;
import com.yar.iot.queryportal.common.response.authentication.AuthenticationResponse;
import com.yar.iot.queryportal.common.response.user.UserResponse;
import com.yar.iot.queryportal.config.security.JwtTokenUtil;
import com.yar.iot.queryportal.domain.enums.Authority;
import com.yar.iot.queryportal.exception.BusinessException;
import com.yar.iot.queryportal.service.BaseService;
import com.yar.iot.queryportal.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Authentication service
 * */
@Service
@RequiredArgsConstructor
public class AuthenticationService extends BaseService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;


    /**
     * Login
     *
     * @param username The username
     * @param password The password
     * @return {@link AuthenticationResponse} response
     * */
    public AuthenticationResponse login(String username, String password) {
        return authenticate(new AuthenticationRequest(username, password));
    }

    /**
     * Authenticate
     *
     * @param request The authenticate request
     * @return {@link AuthenticationResponse} response
     * */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws BusinessException {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final String jwt = jwtTokenUtil.generateToken(authenticate);
            return AuthenticationResponse.builder().token(jwt).build();
        } catch (DisabledException e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid Credential", e);
        }
    }

    /**
     * Get current authenticated user
     *
     * @return {@link UserResponse} response
     * */
    public UserResponse getCurrentUserInfo() {
        return userService.makeResponse(userService.getUserByUsername(getCurrentUser().getName()));
    }

    public UserResponse register(RegisterRequest request) {
        return userService.add(UserRequest.builder()
                .authority(Authority.USER)
                .email(request.getEmail())
                .lastname(request.getLastname())
                .name(request.getName())
                .password(request.getPassword())
                .username(request.getUsername())
                .build());
    }
}
