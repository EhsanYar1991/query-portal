package com.yar.iot.queryportal.common.response.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The authentication response
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Authentication Response")
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = -3454783795249728033L;

    @ApiModelProperty(value = "Token")
    private String token;
}