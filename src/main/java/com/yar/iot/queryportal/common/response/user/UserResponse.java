package com.yar.iot.queryportal.common.response.user;

import com.yar.iot.queryportal.domain.enums.Authority;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User response
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User Response")
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 30023153212627633L;

    private String id;
    private String username;
    private Authority authority;
    private String email;
    private String name;
    private String lastname;
    private Boolean active;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

}
