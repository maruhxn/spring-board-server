package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.TestSupport;
import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends TestSupport {

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldGetMemberDetailWhenIsOwner() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 정보 조회 성공"))
                .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.username").value("tester"))
                .andExpect(jsonPath("$.data.profileImage").value(Constants.BASIC_PROFILE_IMAGE_NAME));
    }

    @Test
    @WithAnonymousUser
    void shouldFailWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        get("/members/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdateProfileWhenIsOwner() throws Exception {
        UpdateMemberProfileRequest dto = new UpdateMemberProfileRequest();
        dto.setUsername("tester!");
        mockMvc.perform(
                        patch(MEMBER_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailWith400WhenUsernameIsShorterThan2() throws Exception {
        UpdateMemberProfileRequest dto = new UpdateMemberProfileRequest();
        dto.setUsername("t");
        mockMvc.perform(
                        patch(MEMBER_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailWith400WhenUsernameIsLongerThan10() throws Exception {
        UpdateMemberProfileRequest dto = new UpdateMemberProfileRequest();
        dto.setUsername("tttttttttttttttttttttttttt");
        mockMvc.perform(
                        patch(MEMBER_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldUpdateProfileFailWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        patch(MEMBER_API_PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdatePasswordWhenIsOwner() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword("updatedTest");
        dto.setConfirmNewPassword("updatedTest");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdatePasswordFailWith400WhenNewPasswordIsShorterThan2() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword(".");
        dto.setConfirmNewPassword(".");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdatePasswordFailWith400WhenNewPasswordIsLongerThan20() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword(".........................................");
        dto.setConfirmNewPassword(".........................................");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdatePPasswordFailWith400WhenIncorrectPassword() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword("updatedPassword");
        dto.setConfirmNewPassword("updatedPassword!");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldUpdatePasswordFailWith401WhenIsAnonymous() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword("updatedPassword");
        dto.setConfirmNewPassword("updatedPassword");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldConfirmPasswordWhenIsOwnerAndCorrectPassword() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("test");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldConfirmPasswordFailWith400WhenIsOwnerAndInCorrectPassword() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("test!");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldConfirmPasswordFailWith400WhenValidationFail() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("t");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldConfirmPasswordFailWith401WhenIsAnonymous() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("test");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldWithdrawWhenIsOwner() throws Exception {
        mockMvc.perform(
                        delete(MEMBER_API_PATH)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldWithdrawFailWhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete("/members/2")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldWithdrawFailWhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete(MEMBER_API_PATH)
                )
                .andExpect(status().isUnauthorized());
    }
}