package com.maruhxn.boardserver.support;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonDocControllerTest extends TestSupport {

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    public void validationErrorSample() throws Exception {
        CommonDocController.SampleRequest sampleRequest = new CommonDocController.SampleRequest("", "");
        mockMvc.perform(
                        post("/test/validation-error")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleRequest))
                )
                .andExpect(status().isBadRequest())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").description("Error Code"),
                                        fieldWithPath("message").description("에러 메시지"),
                                        fieldWithPath("errors").description("Error 값 배열 값"),
                                        fieldWithPath("errors[0].field").description("문제 있는 필드"),
                                        fieldWithPath("errors[0].value").description("문제가 있는 값"),
                                        fieldWithPath("errors[0].reason").description("문재가 있는 이유")
                                )
                        )
                )
        ;
    }

    @Test
    public void globalExceptionSample() throws Exception {
        mockMvc.perform(
                        get("/test/global-exception")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").description("Error Code"),
                                        fieldWithPath("message").description("에러 메시지")
                                )
                        )
                )
        ;
    }
}