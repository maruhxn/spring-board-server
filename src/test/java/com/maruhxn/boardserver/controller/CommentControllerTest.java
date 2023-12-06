package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.domain.Comment;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.repository.CommentRepository;
import com.maruhxn.boardserver.repository.PostRepository;
import com.maruhxn.boardserver.support.CustomWithUserDetails;
import com.maruhxn.boardserver.support.TestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends TestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Comment comment1;
    private Comment comment2;
    private Post savePost;
    String COMMENT_API_PATH;

    @BeforeEach
    void createDummyComment() {
        savePost = postRepository.save(Post.builder()
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

        COMMENT_API_PATH = "/posts/{postId}/comments";
    }

    @Test
    void shouldGetFirst10CommentItemPageWhenNoQueryParameter() throws Exception {
        mockMvc.perform(
                        get(COMMENT_API_PATH, savePost.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(2))
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID")
                                ),
                                queryParameters(
                                        parameterWithName("size").optional().description("조회 결과 크기"),
                                        parameterWithName("page").optional().description("페이지")
                                ),
                                pageResponseFields("CommentItem[]").andWithPrefix("data.results[0].",
                                        fieldWithPath(".commentId").type(NUMBER)
                                                .description("Post ID"),
                                        fieldWithPath(".content").type(STRING)
                                                .description("댓글 내용"),
                                        fieldWithPath(".author").type(OBJECT)
                                                .description("댓글 작성자(CommentAuthor)"),
                                        fieldWithPath(".author.memberId").type(NUMBER)
                                                .description("댓글 작성자 ID"),
                                        fieldWithPath(".author.username").type(STRING)
                                                .description("댓글 작성자 이름"),
                                        fieldWithPath(".author.profileImage").type(STRING)
                                                .description("댓글 작성자 프로필이미지"),
                                        fieldWithPath(".createdAt").type(STRING)
                                                .description("댓글 작성 시각")
                                )
                        )
                );
    }

    @Test
    void shouldGet1CommentItemPageWhenSize1Query() throws Exception {
        mockMvc.perform(
                        get(COMMENT_API_PATH, savePost.getId())
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
    @CustomWithUserDetails
    void shouldCreateCommentWhenIsLoggedIn() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("content");

        simpleRequestConstraints = new ConstraintDescriptions(CreateCommentRequest.class);
        mockMvc.perform(
                        post(COMMENT_API_PATH, savePost.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(STRING)
                                                .description("content")
                                                .attributes(withPath("content"))
                                )
                        )
                );
    }

    @Test
    @WithAnonymousUser
    void shouldFailToCreateCommentWith401WhenIsAnonymous() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("content");

        mockMvc.perform(
                        post(COMMENT_API_PATH, savePost.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToCreateCommentWith400WhenInvalidRequest() throws Exception {
        CreateCommentRequest dto = new CreateCommentRequest();
        dto.setContent("");

        mockMvc.perform(
                        post(COMMENT_API_PATH, savePost.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.VALIDATION_ERROR.getMessage()))
                .andExpect(jsonPath("$.errors.size()").value(1));
    }

    @Test
    @CustomWithUserDetails
    void shouldDeleteCommentWhenIsOwner() throws Exception {
        mockMvc.perform(
                        delete(COMMENT_API_PATH + "/{commentId}", savePost.getId(), comment1.getId())
                )
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID"),
                                        parameterWithName("commentId").description("Comment ID")
                                )
                        )
                );
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToDeleteCommentWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete(COMMENT_API_PATH + "/{commentId}", savePost.getId(), comment2.getId())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToDeleteCommentWithWhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete(COMMENT_API_PATH + "/{commentId}", savePost.getId(), comment1.getId())
                )
                .andExpect(status().isUnauthorized());
    }
}