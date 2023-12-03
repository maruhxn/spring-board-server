package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.TestSupport;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends TestSupport {

    @Autowired
    private PostRepository postRepository;

    private Post post1;
    private Post post2;

    @BeforeEach
    void createDummyPost() {
        post1 = postRepository.save(Post.builder()
                .member(member)
                .title("title1")
                .content("content1")
                .viewCount(0L)
                .build()
        );

        post2 = postRepository.save(Post.builder()
                .member(admin)
                .title("title2")
                .content("content2")
                .viewCount(0L)
                .build()
        );
    }

    @Test
    void shouldGet10PostItemPageWhenNoQueryParameter() throws Exception {
        mockMvc.perform(
                        get("/posts")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(2));
    }

    @Test
    void shouldGet1PostItemPageWhenSize1Query() throws Exception {
        mockMvc.perform(
                        get("/posts")
                                .queryParam("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(false))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(1));
    }

    @Test
    void shouldGetPostItemPageOfTesterWhenAuthorTesterQuery() throws Exception {
        mockMvc.perform(
                        get("/posts")
                                .queryParam("author", "tester")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.results.size()").value(1));
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldCreatePostWhenIsLoggedInWithValidRequest() throws Exception {
        CreatePostRequest dto = new CreatePostRequest();
        dto.setTitle("title");
        dto.setContent("content");

        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToCreatePostWith401WhenIsAnonymous() throws Exception {
        CreatePostRequest dto = new CreatePostRequest();
        dto.setTitle("title");
        dto.setContent("content");

        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToCreatePostWith400WhenIsAnonymous() throws Exception {
        CreatePostRequest dto = new CreatePostRequest();
        dto.setTitle("");
        dto.setContent("");

        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.messages.size()").value(2));
    }

    @Test
    void shouldGetPostDetailWhenIsExist() throws Exception {
        mockMvc.perform(
                        get("/posts/" + post1.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 조회 성공"))
                .andExpect(jsonPath("$.data.postId").value(post1.getId()))
                .andExpect(jsonPath("$.data.title").value(post1.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post1.getContent()))
                .andExpect(jsonPath("$.data.images.size()").value(0))
                .andExpect(jsonPath("$.data.authorName").value(post1.getMember().getUsername()))
                .andExpect(jsonPath("$.data.viewCount").value(post1.getViewCount()))
                .andExpect(jsonPath("$.data.createdAt").exists());
    }

    @Test
    void shouldFailToGetPostDetailWhenIsNotExist() throws Exception {
        mockMvc.perform(
                        get("/posts/" + post1.getId() + 3)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_POST.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_POST.getMessage()));
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldUpdatePostWhenIsOwnerAndValidRequest() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("title!");
        dto.setContent("content!");

        mockMvc.perform(
                        patch("/posts/" + post1.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToUpdatePostWith401WhenIsAnonymous() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("test!");
        dto.setContent("content!");

        mockMvc.perform(
                        patch("/posts/" + post1.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToUpdatePostWith403WhenIsNotOwner() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("test!");
        dto.setContent("content!");

        mockMvc.perform(
                        patch("/posts/" + post2.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldDeletePostWhenIsOwner() throws Exception {
        mockMvc.perform(
                        delete("/posts/" + post1.getId())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToDeletePostWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete("/posts/" + post2.getId())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToDeleteImageWithWhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete("/posts/" + post1.getId())
                )
                .andExpect(status().isUnauthorized());
    }
}