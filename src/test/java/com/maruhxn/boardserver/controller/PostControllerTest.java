package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.PostImage;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.repository.PostRepository;
import com.maruhxn.boardserver.service.FileService;
import com.maruhxn.boardserver.support.CustomWithUserDetails;
import com.maruhxn.boardserver.support.TestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.io.InputStream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends TestSupport {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileService fileService;

    private Post post1;
    private Post post2;
    String POST_API_PATH = "/posts";

    @BeforeEach
    void createDummyPost() {

        post1 = postRepository.save(Post.builder()
                .member(member)
                .title("title1")
                .content("content1")
                .viewCount(0L)
                .build()
        );

        PostImage post1Image = PostImage.builder()
                .post(post1)
                .originalName("defaultProfileImage.jfif")
                .storedName("stored-default-profile-image.jfif")
                .build();

        post1.addPostImage(post1Image);

        post2 = postRepository.save(Post.builder()
                .member(admin)
                .title("title2")
                .content("content2")
                .viewCount(0L)
                .build()
        );
    }

    @Test
    void shouldGetFirst10PostItemPageWhenNoQueryParameter() throws Exception {
        mockMvc.perform(
                        get(POST_API_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 리스트 조회 성공"))
                .andExpect(jsonPath("$.data.isFirst").value(true))
                .andExpect(jsonPath("$.data.isLast").value(true))
                .andExpect(jsonPath("$.data.isEmpty").value(false))
                .andExpect(jsonPath("$.data.totalPage").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.results.size()").value(2))
                .andDo(
                        restDocs.document(
                                queryParameters(
                                        parameterWithName("size").optional().description("조회 결과 크기"),
                                        parameterWithName("page").optional().description("페이지"),
                                        parameterWithName("title").optional().description("제목 검색"),
                                        parameterWithName("content").optional().description("내용 검색"),
                                        parameterWithName("author").optional().description("작성자 이름 검색")
                                ),
                                pageResponseFields("PostItem[]")
                                        .andWithPrefix("data.results[0].",
                                                fieldWithPath("postId").type(NUMBER)
                                                        .description("Post ID"),
                                                fieldWithPath("title").type(STRING)
                                                        .description("게시글 제목"),
                                                fieldWithPath("authorName").type(STRING)
                                                        .description("게시글 작성자 이름"),
                                                fieldWithPath("createdAt").type(STRING)
                                                        .description("게시글 생성 시각"),
                                                fieldWithPath("viewCount").type(NUMBER)
                                                        .description("게시글 조회 수"),
                                                fieldWithPath("commentCount").type(NUMBER)
                                                        .description("게시글 댓글 수")
                                        )
                        )
                );
    }

    @Test
    void shouldGet1PostItemPageWhenSize1Query() throws Exception {
        mockMvc.perform(
                        get(POST_API_PATH)
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
                        get(POST_API_PATH)
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
    @CustomWithUserDetails
    void shouldCreatePostWithOneImageWhenIsLoggedIn() throws Exception {
        final String originalFileName = "defaultProfileImage.jfif"; //파일명
        String filePath = "src/test/resources/static/img/" + originalFileName;
        FileSystemResource resource = new FileSystemResource(filePath);
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile image1 = new MockMultipartFile("images", originalFileName, "image/jpeg", inputStream);


        simpleRequestConstraints = new ConstraintDescriptions(CreatePostRequest.class);
        mockMvc.perform(
                        multipart(POST_API_PATH)
                                .file(image1)
                                .part(new MockPart("title", "title".getBytes()))
                                .part(new MockPart("content", "content".getBytes()))
                )
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestParts(
                                        partWithName("title")
                                                .description("title")
                                                .attributes(setType("string"))
                                                .attributes(withPath("title")),
                                        partWithName("content")
                                                .description("content")
                                                .attributes(setType("string"))
                                                .attributes(withPath("content")),
                                        partWithName("images")
                                                .description("images")
                                                .attributes(setType("List<MultiPartFile>"))
                                                .attributes(withPath("images"))
                                )
                        ));
    }

    @Test
    @WithAnonymousUser
    void shouldFailToCreatePostWith401WhenIsAnonymous() throws Exception {
        CreatePostRequest dto = new CreatePostRequest();
        dto.setTitle("title");
        dto.setContent("content");

        mockMvc.perform(
                        post(POST_API_PATH)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToCreatePostWith400WhenIsInvalidRequest() throws Exception {
        CreatePostRequest dto = new CreatePostRequest();
        dto.setTitle("");
        dto.setContent("");

        mockMvc.perform(
                        multipart(POST_API_PATH)
                                .part(new MockPart("title", dto.getTitle().getBytes()))
                                .part(new MockPart("content", dto.getContent().getBytes()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPostDetailWhenIsExist() throws Exception {

        mockMvc.perform(
                        get("/posts/{postId}", post1.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 조회 성공"))
                .andExpect(jsonPath("$.data.postId").value(post1.getId()))
                .andExpect(jsonPath("$.data.title").value(post1.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post1.getContent()))
                .andExpect(jsonPath("$.data.images.size()").value(1))
                .andExpect(jsonPath("$.data.author.memberId").value(post1.getMember().getId()))
                .andExpect(jsonPath("$.data.author.username").value(post1.getMember().getUsername()))
                .andExpect(jsonPath("$.data.author.profileImage").value(post1.getMember().getProfileImage()))
                .andExpect(jsonPath("$.data.viewCount").value(post1.getViewCount()))
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID")
                                ),
                                commonResponseFields("PostDetailItem").andWithPrefix("data.",
                                        fieldWithPath("postId").type(NUMBER)
                                                .description("Post ID"),
                                        fieldWithPath("title").type(STRING)
                                                .description("게시글 제목"),
                                        fieldWithPath("content").type(STRING)
                                                .description("게시글 내용"),
                                        fieldWithPath("images").type(ARRAY)
                                                .description("PostImageItem[]"),
                                        fieldWithPath("author").type(OBJECT)
                                                .description("게시글 작성자(CommentAuthor)"),
                                        fieldWithPath("viewCount").type(NUMBER)
                                                .description("게시글 조회 수"),
                                        fieldWithPath("createdAt").type(STRING)
                                                .description("게시글 생성 시각")
                                ).andWithPrefix("data.images[0].",
                                        fieldWithPath("imageId").type(NUMBER)
                                                .description("게시글 이미지 아이디"),
                                        fieldWithPath("originalName").type(STRING)
                                                .description("게시글 이미지 원본 이름"),
                                        fieldWithPath("storedName").type(STRING)
                                                .description("게시글 이미지 저장된 이름")
                                ).andWithPrefix("data.author.",
                                        fieldWithPath("memberId").type(NUMBER)
                                                .description("게시글 작성자 ID"),
                                        fieldWithPath("username").type(STRING)
                                                .description("게시글 작성자 이름"),
                                        fieldWithPath("profileImage").type(STRING)
                                                .description("게시글 작성자 프로필이미지")
                                )
                        )
                );
    }

    @Test
    void shouldFailToGetPostDetailWith404WhenIsNotExist() throws Exception {
        mockMvc.perform(
                        get("/posts/{postId}", post1.getId() + 3)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND_POST.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_POST.getMessage()));
    }

    @Test
    @CustomWithUserDetails
    void shouldUpdatePostWhenIsOwner() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("title!");
        dto.setContent("content!");

        simpleRequestConstraints = new ConstraintDescriptions(UpdatePostRequest.class);
        mockMvc.perform(
                        patch("/posts/{postId}", post1.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID")
                                ),
                                formParameters(
                                        parameterWithName("title").optional()
                                                .description("title")
                                                .attributes(setType("string"))
                                                .attributes(withPath("title")),
                                        parameterWithName("content").optional()
                                                .description("content")
                                                .attributes(setType("string"))
                                                .attributes(withPath("content")),
                                        parameterWithName("images").optional()
                                                .description("images")
                                                .attributes(setType("List<MultipartFile>"))
                                                .attributes(withPath("images"))
                                )
                        )
                );
    }

    @Test
    @WithAnonymousUser
    void shouldFailToUpdatePostWith401WhenIsAnonymous() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("test!");
        dto.setContent("content!");

        mockMvc.perform(
                        patch("/posts/{postId}", post1.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToUpdatePostWith403WhenIsNotOwner() throws Exception {
        UpdatePostRequest dto = new UpdatePostRequest();
        dto.setTitle("test!");
        dto.setContent("content!");

        mockMvc.perform(
                        patch("/posts/{postId}", post2.getId())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("title", dto.getTitle())
                                .param("content", dto.getContent()))
                .andExpect(status().isForbidden());
    }

    @Test
    @CustomWithUserDetails
    void shouldDeletePostWhenIsOwner() throws Exception {
        mockMvc.perform(
                        delete("/posts/{postId}", post1.getId())
                )
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("postId").description("Post ID")
                                )
                        )
                );
    }

    @Test
    @CustomWithUserDetails
    void shouldFailToDeletePostWith403WhenNoAuthorities() throws Exception {
        mockMvc.perform(
                        delete("/posts/{postId}", post2.getId())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void shouldFailToDeletePostWithWhenIsAnonymous() throws Exception {
        mockMvc.perform(
                        delete("/posts/{postId}", post1.getId())
                )
                .andExpect(status().isUnauthorized());
    }
}