package com.yar.iot.queryportal.controller.user;

import com.yar.iot.queryportal.common.request.user.UserRequest;
import com.yar.iot.queryportal.common.response.user.UserResponse;
import com.yar.iot.queryportal.controller.CrudController;
import com.yar.iot.queryportal.service.user.UserService;
import io.swagger.annotations.Api;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller
 *
 * @see com.yar.iot.queryportal.controller.CrudController
 * @see com.yar.iot.queryportal.controller.BaseController
 * */
@RestController
@RequestMapping(UserController.USER_URL_CONTEXT)
@Api(value = "User APIs")
public class UserController extends CrudController<UserRequest, UserResponse> {

    public static final String USER_URL_CONTEXT = "/user";
    public static final String CHANGE_PASSWORD_URL_CONTEXT = "/change-password/{id}";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    /**
     * Change password API
     *
     * @param id The user ID
     * @param oldPassword The old password
     * @param newPassword The new password
     * @return {@link ResponseEntity<UserResponse>}
     * */
    @PreAuthorize(ALL_AUTHORITY)
    @PutMapping(CHANGE_PASSWORD_URL_CONTEXT)
    public ResponseEntity<UserResponse> changePassword(@PathVariable(name = "id") @NotNull(message = "id must be determined.") String id,
                                            @RequestParam(name = "oldPassword") @NotBlank(message = "oldPassword must be determined.") String oldPassword,
                                            @RequestParam(name = "newPassword") @NotBlank(message = "newPassword must be determined.") String newPassword) {
        return okResponse(userService.changePassword(id, oldPassword, newPassword));
    }

}
