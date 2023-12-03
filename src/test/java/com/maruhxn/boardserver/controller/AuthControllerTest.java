package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.TestSupport;
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
    void Should_Register_SuccessFully_When_isAnonymous() throws Exception {
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
    void Should_Register_Fail_With_403_When_Is_Logged_In() throws Exception {
        RegisterRequest registerRequest = getRegisterRequest();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void Should_Register_Fail_With_400_When_IllegalRequest() throws Exception {
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
                .andExpect(jsonPath("$.messages.size()").value(2));
    }

    @Test
    @WithAnonymousUser
    void Should_Register_Fail_With_400_When_Password_Confirm_Fail() throws Exception {
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
    void Should_Register_Fail_With_422_When_Already_Existing_User() throws Exception {
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
    void Should_Login_SuccessFully_When_isAnonymous() throws Exception {
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
    void Should_Login_Fail_With_401_When_IllegalMethod() throws Exception {
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
    void Should_Login_Fail_With_400_When_IllegalRequest() throws Exception {
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
    void Should_Logout_Successfully_When_Is_Not_Logged_In() throws Exception {
        mockMvc.perform(delete("/auth/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    void Should_Logout_Fail_With_401_When_Is_Not_Logged_In() throws Exception {
        mockMvc.perform(delete("/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

}