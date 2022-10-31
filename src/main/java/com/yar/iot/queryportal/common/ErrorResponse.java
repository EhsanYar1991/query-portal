package com.yar.iot.queryportal.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The custom error response
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ApiModel(description = "Error Response")
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = -6158306544000383963L;

    @ApiModelProperty(value = "The error message")
    private String errorMessage;

}
