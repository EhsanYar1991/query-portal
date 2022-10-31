package com.yar.iot.queryportal.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yar.iot.queryportal.common.ErrorResponse;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * The custom JWT authentication entry point for managing the authentication exceptions.
 * */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final String UNAUTHORIZED_MSG= "Unauthorized";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED_MSG))
        );
    }
}
