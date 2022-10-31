package com.yar.iot.queryportal.common.request.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The authentication request
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Authentication Request")
public class AuthenticationRequest implements Serializable {

    @NotBlank(message = "username must be determined.")
    @Size(max = 35)
    @ApiModelProperty(value = "Username")
    private String username;

    @Size(max = 10)
    @NotBlank(message = "password must be determined.")
    @ApiModelProperty(value = "Password")
    private String password;
}