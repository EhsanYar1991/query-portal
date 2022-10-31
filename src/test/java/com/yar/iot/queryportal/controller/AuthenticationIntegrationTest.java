package com.yar.iot.queryportal.controller;

import com.yar.iot.queryportal.tests.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for authentication
 * */
class AuthenticationIntegrationTest extends AbstractIntegrationTest {

    private static final String USERNAME = "eyar";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "eyar@domain.com";
    private static final String NAME = "Ehsan";
    private static final String LASTNAME = "Yar";

    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Test
    void loginWithAdminTest() throws Exception {
        mockMvc.perform(post("/auth/login")
                .queryParam("username", adminUsername)
                .queryParam("password", adminPassword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void registerWithUserTest() throws Exception {
        mockMvc.perform(post("/auth/register")
                .queryParam("username", USERNAME)
                .queryParam("password", PASSWORD)
                .queryParam("email", EMAIL)
                .queryParam("name", NAME)
                .queryParam("lastname", LASTNAME))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.lastname", is(LASTNAME)))
                .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    void registerAndLoginWithUserTest() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .queryParam("username", "TestUser")
                        .queryParam("password", "password")
                        .queryParam("email", "test@test.com")
                        .queryParam("name", "test")
                        .queryParam("lastname", "test"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/auth/login")
                .queryParam("username", "TestUser")
                .queryParam("password", "password"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }
}
