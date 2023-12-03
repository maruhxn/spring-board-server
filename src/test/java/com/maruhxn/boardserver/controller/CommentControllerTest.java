package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.TestSupport;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.domain.Comment;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.repository.CommentRepository;
import com.maruhxn.boardserver.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends TestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Comment comment1;
    private Comment comment2;
    String BASE_COMMENT_API_PATH;

    @BeforeEach
    void createDummyComment() {
        Post savePost = postRepository.save(Post.builder()
                .member(member)
                .title("title")
                .content("content")
                .viewCount(0L)
                .build()
        );

        comment1 = Comment.builder()
                .content("commentContent")
                .member(member)
                .post(savePost)
                .build();

        comment2 = Comment.builder()
                .content("commentContent")
                .member(admin)
                .post(savePost)
                .build();

        List<Comment> comments = List.of(comment1, comment2);

        commentRepository.saveAll(comments);

        BASE_COMMENT_API_PATH = "/posts/" + savePost.getId() + "/comments";
    }

    @Test
    void shouldGetFirst10CommentItemPageWhenNoQueryParameter() throws Exception {
        mockMvc.perform(
                        get(BASE_COMMENT_API_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(2));
    }

    @Test
    void shouldGet1CommentItemPageWhenSize1Query() throws Exception {
        mockMvc.perform(
                        get(BASE_COMMENT_API_PATH)
                                .queryParam("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(false))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(1));
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldCreateCommentWhenIsLoggedInWithValidRequest() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("content");

        mockMvc.perform(
                        post(BASE_COMMENT_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToCreateCommentWith401WhenIsAnonymous() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("content");

        mockMvc.perform(
                        post(BASE_COMMENT_API_PATH)
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
    void shouldFailToCreateCommentWith400WhenIsInvalidRequest() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("");

        mockMvc.perform(
                        post(BASE_COMMENT_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.messages.size()").value(1));
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldDeleteCommentWhenIsOwner() throws Exception {
        mockMvc.perform(
                        delete(BASE_COMMENT_API_PATH + "/" + comment1.getId())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(
            value = "test@test.com",
            userDetailsServiceBeanName = "ajaxUserDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldFailToDeleteCommentWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete(BASE_COMMENT_API_PATH + "/" + comment2.getId())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToDeleteCommentWithWhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete(BASE_COMMENT_API_PATH + "/" + comment1.getId())
                )
                .andExpect(status().isUnauthorized());
    }
}