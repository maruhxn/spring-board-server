package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.support.TestSupport;
import com.maruhxn.boardserver.common.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileControllerTest extends TestSupport {
    @Test
    void shouldGetPImageWhenIsExist() throws Exception {
        mockMvc.perform(
                        get("/images/{fileName}", Constants.BASIC_PROFILE_IMAGE_NAME)
                                .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("fileName").description("Image Name")
                                )
                        )
                );
    }
}