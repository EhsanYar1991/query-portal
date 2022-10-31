package com.yar.iot.queryportal.common.response.record;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Parameter statistics response
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamStatisticsResponse implements Serializable {

    private static final long serialVersionUID = 6937614876653712154L;

    @ApiModelProperty(value = "Count")
    private Long count;

    @ApiModelProperty(value = "Max")
    private Double max;

    @ApiModelProperty(value = "Min")
    private Double min;

    @ApiModelProperty(value = "Average")
    private Double average;

    @ApiModelProperty(value = "Median")
    private Double median;
}
