package com.yar.iot.queryportal.common.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration request object
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Registration Request")
public class RegisterRequest {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @NotBlank(message = "username must be determined.")
    @ApiModelProperty(value = "Username")
    private String username;

    @NotBlank(message = "password must be determined.")
    @ApiModelProperty(value = "Password")
    private String password;

    @NotBlank(message = "email must be determined.")
    @Email(message = "email format is not valid.", regexp = EMAIL_REGEX)
    @ApiModelProperty(value = "Email")
    private String email;

    @NotBlank(message = "name must be determined.")
    @ApiModelProperty(value = "Name")
    private String name;

    @NotBlank(message = "lastname must be determined.")
    @ApiModelProperty(value = "Lastname")
    private String lastname;
}
