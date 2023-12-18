package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.support.CustomWithUserDetails;
import com.maruhxn.boardserver.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends TestSupport {

    @Test
    @CustomWithUserDetails
    void shouldGetMemberInfoWhenIsAuthenticated() throws Exception {
        mockMvc.perform(
                        get("/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("로그인 회원 정보"))
                .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.username").value("tester"))
                .andExpect(jsonPath("$.data.profileImage").value(Constants.BASIC_PROFILE_IMAGE_NAME))
                .andDo(
                        restDocs.document(
                                commonResponseFields("MemberDetail")
                                        .andWithPrefix("data.",
                                                fieldWithPath("memberId").type(NUMBER)
                                                        .description("Member ID"),
                                                fieldWithPath("email").type(STRING)
                                                        .description("email"),
                                                fieldWithPath("username").type(STRING)
                                                        .description("username"),
                                                fieldWithPath("profileImage").type(STRING)
                                                        .description("profileImage")
                                        )
                        )
                );
    }

    @Test
    @WithAnonymousUser
    void shouldFailToGetMemberInfoWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        get("/auth"))
                .andExpect(status().isUnauthorized());
    }

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
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test1@test.com")
                .username("")
                .password("test")
                .confirmPassword("test")
                .build();

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
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test1@test.com")
                .username("tester")
                .password("test")
                .confirmPassword("testt")
                .build();
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
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@test.com")
                .username("tester")
                .password("test")
                .confirmPassword("test")
                .build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(ErrorCode.EXISTING_USER.name()));
    }

    private static RegisterRequest getRegisterRequest() {
        return RegisterRequest.builder()
                .email("test1@test.com")
                .username("tester1")
                .password("test")
                .confirmPassword("test")
                .build();
    }

    @Test
    @WithAnonymousUser
    void shouldLoginWhenIsAnonymous() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@test.com")
                .password("test")
                .build();
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
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@test.com")
                .password("test")
                .build();

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
        LoginRequest loginRequest = LoginRequest.builder()
                .email("testtest@test.com")
                .password("")
                .build();

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
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