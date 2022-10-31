package com.yar.iot.queryportal.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The pagination response
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ApiModel(description = "Pagination Response")
public class PaginationResponse<RESPONSE> implements Serializable {

    private static final long serialVersionUID = 1131936641500814810L;

    @ApiModelProperty(value = "Total size")
    private long totalSize;

    @ApiModelProperty(value = "Total page")
    private long totalPage;

    @ApiModelProperty(value = "Page number")
    private int page;

    @ApiModelProperty(value = "Page size")
    private int size;

    @ApiModelProperty(value = "The list of response")
    private List<RESPONSE> list;
}
