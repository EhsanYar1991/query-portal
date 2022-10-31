package com.yar.iot.queryportal.controller.authentication;


import com.yar.iot.queryportal.common.request.authentication.AuthenticationRequest;
import com.yar.iot.queryportal.common.request.user.RegisterRequest;
import com.yar.iot.queryportal.common.response.authentication.AuthenticationResponse;
import com.yar.iot.queryportal.common.response.user.UserResponse;
import com.yar.iot.queryportal.controller.BaseController;
import com.yar.iot.queryportal.service.authentication.AuthenticationService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller
 *
 * @see com.yar.iot.queryportal.controller.BaseController
 */
@RestController
@RequestMapping(AuthenticationController.AUTHENTICATION_CONTEXT_URL)
@AllArgsConstructor
@Api(value = "Authentication APIs")
public class AuthenticationController implements BaseController {

    public static final String AUTHENTICATION_CONTEXT_URL = "/auth";
    private static final String LOGIN_URL = "/login";
    private static final String AUTHENTICATE_URL = "/authenticate";
    private static final String CURRENT_USER_INFO = "/user-info";
    public static final String USER_REGISTRATION = "/register";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String USERNAME_VALIDATION_MSG = "username must be determined.";
    private static final String PASSWORD_VALIDATION_MSG = "password must be determined.";

    private final AuthenticationService authenticationService;

    /**
     * Login API
     *
     * @param username The username
     * @param password The password
     * @return {@link ResponseEntity<AuthenticationResponse>}
     */
    @PostMapping(LOGIN_URL)
    public ResponseEntity<AuthenticationResponse> login(@NotBlank(message = USERNAME_VALIDATION_MSG) @RequestParam(USERNAME_PARAM) String username,
            @NotBlank(message = PASSWORD_VALIDATION_MSG) @RequestParam(PASSWORD_PARAM) String password) {
        return okResponse(authenticationService.login(username, password));
    }

    /**
     * Authenticate API
     *
     * @param authenticationRequest The authentication request
     * @return {@link ResponseEntity<AuthenticationResponse>}
     */
    @PostMapping(value = AUTHENTICATE_URL)
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return okResponse(authenticationService.authenticate(authenticationRequest));
    }

    /**
     * Get the current authenticated user API
     *
     * @return {@link ResponseEntity<UserResponse>}
     */
    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping(value = CURRENT_USER_INFO)
    public ResponseEntity<UserResponse> getCurrentUserInfo() {
        return okResponse(authenticationService.getCurrentUserInfo());
    }

    /**
     * Registration API
     *
     * @param request The registration request
     * @return {@link ResponseEntity<AuthenticationResponse>}
     */
    @PostMapping(USER_REGISTRATION)
    public ResponseEntity<UserResponse> register(@Valid RegisterRequest request) {
        return responseBuilder(HttpStatus.CREATED, authenticationService.register(request));
    }

}