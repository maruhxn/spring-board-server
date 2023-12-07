package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.support.CustomWithUserDetails;
import com.maruhxn.boardserver.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends TestSupport {
    String MEMBER_API_PATH = "/members/{memberId}";

    @Test
    @CustomWithUserDetails
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
    void shouldFailToGetMemberDetailWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH, member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToGetMemberDetailWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        get(MEMBER_API_PATH, member.getId() + 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @CustomWithUserDetails
    void shouldUpdateProfileWhenIsOwner() throws Exception {
        MockMultipartHttpServletRequestBuilder builder = getMockMultipartHttpServletRequestBuilder();
        simpleRequestConstraints = new ConstraintDescriptions(UpdateMemberProfileRequest.class);
        final String originalFileName = "defaultProfileImage.jfif"; //파일명
        String filePath = "src/test/resources/static/img/" + originalFileName;
        FileSystemResource resource = new FileSystemResource(filePath);
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile image1 = new MockMultipartFile("profileImage", originalFileName, "image/jpeg", inputStream);

        mockMvc.perform(
                        builder
                                .part(new MockPart("username", "tester!".getBytes()))
                                .file(image1)
                )
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("memberId").description("Member ID")
                                ),
                                requestParts(
                                        partWithName("username")
                                                .description("username")
                                                .attributes(setType("string"))
                                                .attributes(withPath("username")),
                                        partWithName("profileImage")
                                                .description("profileImage")
                                                .attributes(setType("MultiPartFile"))
                                                .attributes(withPath("profileImage"))
                                )
                        )
                );
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToUpdateProfileWith400WhenUsernameIsShorterThan2() throws Exception {
        MockMultipartHttpServletRequestBuilder builder = getMockMultipartHttpServletRequestBuilder();

        mockMvc.perform(
                        builder
                                .part(new MockPart("username", "t".getBytes()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToUpdateProfileWith400WhenUsernameIsLongerThan10() throws Exception {
        MockMultipartHttpServletRequestBuilder builder = getMockMultipartHttpServletRequestBuilder();

        mockMvc.perform(
                        builder
                                .part(new MockPart("username", "tttttttttttttttttttttttttt".getBytes()))
                )
                .andExpect(status().isBadRequest());
    }

    private MockMultipartHttpServletRequestBuilder getMockMultipartHttpServletRequestBuilder() {
        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.
                multipart(MEMBER_API_PATH, member.getId());

        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod(HttpMethod.PATCH.name());
                return request;
            }
        });
        return builder;
    }

    @Test
    @WithAnonymousUser
    void shouldFailToUpdateProfileWith401WhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        patch(MEMBER_API_PATH, member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
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
                                        fieldWithPath("currPassword").type(STRING)
                                                .description("currPassword")
                                                .attributes(withPath("currPassword")),
                                        fieldWithPath("newPassword").type(STRING)
                                                .description("newPassword")
                                                .attributes(withPath("newPassword")),
                                        fieldWithPath("confirmNewPassword").type(STRING)
                                                .description("confirmNewPassword")
                                                .attributes(withPath("confirmNewPassword"))
                                )
                        )
                );
    }

    @Test
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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
                                commonResponseFields(null)
                        )
                );
    }

    @Test
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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
    @CustomWithUserDetails
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