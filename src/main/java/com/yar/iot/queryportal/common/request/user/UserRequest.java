package com.yar.iot.queryportal.common.request.user;

import com.yar.iot.queryportal.domain.enums.Authority;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User request object
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "User Request")
public class UserRequest {

    private static final String EMAIL_REGEX = "(^[A-Za-z0-9+_.-]+@(.+)$)";

    @NotBlank(message = "username must be determined.")
    @ApiModelProperty(value = "Username")
    private String username;

    @NotBlank(message = "username must be determined.")
    @ApiModelProperty(value = "Password")
    private String password;

    @NotNull(message = "authority must be determined.")
    @ApiModelProperty(value = "Authority")
    private Authority authority;

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
