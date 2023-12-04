package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends TestSupport {
    String MEMBER_API_PATH = "/members/{memberId}";

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldGetMemberDetailWhenIsOwner() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH, member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 정보 조회 성공"))
                .andExpect(jsonPath("$.data.memberId").value(member.getId()))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.username").value("tester"))
                .andExpect(jsonPath("$.data.profileImage").value(Constants.BASIC_PROFILE_IMAGE_NAME))
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(STRING)
                                                .description("상태 코드"),
                                        fieldWithPath("message").type(STRING)
                                                .description("상태 메세지"),
                                        fieldWithPath("data.memberId").type(NUMBER)
                                                .description("Member ID"),
                                        fieldWithPath("data.email").type(STRING)
                                                .description("email"),
                                        fieldWithPath("data.username").type(STRING)
                                                .description("username"),
                                        fieldWithPath("data.profileImage").type(STRING)
                                                .description("profileImage")
                                )
                        )
                );
    }

    @Test
    @WithAnonymousUser
    void shouldFailToGetMemberDetailWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH, member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToGetMemberDetailWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH, member.getId() + 1))
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

        simpleRequestConstraints = new ConstraintDescriptions(UpdateMemberProfileRequest.class);

        mockMvc.perform(
                        patch(MEMBER_API_PATH, member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                requestFields(
                                        fieldWithPath("username").type(STRING).description("username").attributes(withPath("username")),
                                        fieldWithPath("profileImage").type(MultipartFile.class).optional().description("profileImage")
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
    void shouldFailToUpdateProfileWith400WhenUsernameIsShorterThan2() throws Exception {
        UpdateMemberProfileRequest dto = new UpdateMemberProfileRequest();
        dto.setUsername("t");
        mockMvc.perform(
                        patch(MEMBER_API_PATH, member.getId())
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
    void shouldFailToUpdateProfileWith400WhenUsernameIsLongerThan10() throws Exception {
        UpdateMemberProfileRequest dto = new UpdateMemberProfileRequest();
        dto.setUsername("tttttttttttttttttttttttttt");
        mockMvc.perform(
                        patch(MEMBER_API_PATH, member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToUpdateProfileWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        patch(MEMBER_API_PATH, member.getId()))
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

        simpleRequestConstraints = new ConstraintDescriptions(UpdatePasswordRequest.class);
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                requestFields(
                                        fieldWithPath("currPassword").type(STRING).description("currPassword").attributes(withPath("currPassword")),
                                        fieldWithPath("newPassword").type(STRING).description("newPassword").attributes(withPath("newPassword")),
                                        fieldWithPath("confirmNewPassword").type(STRING).description("confirmNewPassword").attributes(withPath("confirmNewPassword"))
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
    void shouldFailToUpdatePasswordWith400WhenNewPasswordIsShorterThan2() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword(".");
        dto.setConfirmNewPassword(".");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password", member.getId())
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
    void shouldFailToUpdatePasswordWith400WhenNewPasswordIsLongerThan20() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword(".........................................");
        dto.setConfirmNewPassword(".........................................");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password", member.getId())
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
    void shouldFailToUpdatePasswordWith400WhenIncorrectPassword() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword("updatedPassword");
        dto.setConfirmNewPassword("updatedPassword!");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToUpdatePasswordWith401WhenIsAnonymous() throws Exception {
        UpdatePasswordRequest dto = new UpdatePasswordRequest();
        dto.setCurrPassword("test");
        dto.setNewPassword("updatedPassword");
        dto.setConfirmNewPassword("updatedPassword");
        mockMvc.perform(
                        patch(MEMBER_API_PATH + "/change-password", member.getId())
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
        simpleRequestConstraints = new ConstraintDescriptions(ConfirmPasswordRequest.class);
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId")
                                                .description("Member ID")
                                ),
                                requestFields(
                                        fieldWithPath("currPassword").type(STRING)
                                                .description("currPassword")
                                                .attributes(withPath("currPassword"))
                                ),
                                responseFields(
                                        fieldWithPath("code").type(STRING)
                                                .description("상태 코드"),
                                        fieldWithPath("message").type(STRING)
                                                .description("상태 메시지")
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
    void shouldFailToConfirmPasswordWith400WhenIncorrectPassword() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("test!");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password", member.getId())
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
    void shouldFailToConfirmPasswordWith400WhenInvalidRequest() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("t");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToConfirmPasswordWith401WhenIsAnonymous() throws Exception {
        ConfirmPasswordRequest dto = new ConfirmPasswordRequest();
        dto.setCurrPassword("test");
        mockMvc.perform(
                        post(MEMBER_API_PATH + "/confirm-password", member.getId())
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
                        delete(MEMBER_API_PATH, member.getId())
                )
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
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
    void shouldFailToWithdrawWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete(MEMBER_API_PATH, member.getId() + 1)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToWithdrawWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete(MEMBER_API_PATH, member.getId())
                )
                .andExpect(status().isUnauthorized());
    }
}