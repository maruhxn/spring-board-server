package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.support.TestSupport;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends TestSupport {

    @Test
    @WithAnonymousUser
    void shouldRegisterWhenIsAnonymous() throws Exception {
        RegisterRequest registerRequest = getRegisterRequest();
        simpleRequestConstraints = new ConstraintDescriptions(RegisterRequest.class);
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("email").attributes(withPath("email")),
                                        fieldWithPath("username").type(STRING).description("username").attributes(withPath("username")),
                                        fieldWithPath("password").type(STRING).description("password").attributes(withPath("password")),
                                        fieldWithPath("confirmPassword").type(STRING).description("confirmPassword").attributes(withPath("confirmPassword"))
                                )
                        )
                );
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToRegisterWith403WhenIsLoggedIn() throws Exception {
        RegisterRequest registerRequest = getRegisterRequest();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToRegisterWith400WhenIsInvalidRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test1@test.com");
        registerRequest.setUsername("");
        registerRequest.setPassword("test");
        registerRequest.setConfirmPassword("test");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()))
                .andExpect(jsonPath("$.errors.size()").value(2));
    }

    @Test
    @WithAnonymousUser
    void shouldFailToRegisterWith400WhenConfirmFail() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test1@test.com");
        registerRequest.setUsername("tester");
        registerRequest.setPassword("test");
        registerRequest.setConfirmPassword("testt");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.PASSWORD_CONFIRM_FAIL.name()));
    }

    @Test
    @WithAnonymousUser
    void shouldFailToRegisterWith422WhenIsExistingUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setUsername("tester");
        registerRequest.setPassword("test");
        registerRequest.setConfirmPassword("test");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(ErrorCode.EXISTING_USER.name()));
    }

    private static RegisterRequest getRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test1@test.com");
        registerRequest.setUsername("tester1");
        registerRequest.setPassword("test");
        registerRequest.setConfirmPassword("test");
        return registerRequest;
    }

    @Test
    @WithAnonymousUser
    void shouldLoginWhenIsAnonymous() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("test");
        simpleRequestConstraints = new ConstraintDescriptions(LoginRequest.class);
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email").type(STRING).description("email").attributes(withPath("email")),
                                        fieldWithPath("password").type(STRING).description("password").attributes(withPath("password"))
                                )
                        )
                );
    }

    @Test
    @WithAnonymousUser
    void shouldFailToLoginWith401WhenIsInvalidMethod() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("test");

        mockMvc.perform(
                        get("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToLoginWith400WhenIsInvalidRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testtest.com");
        loginRequest.setPassword("");

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldLogoutWhenIsLoggedIn() throws Exception {
        mockMvc.perform(delete("/auth/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToLogoutWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(delete("/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

}