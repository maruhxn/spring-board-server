package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.auth.common.AccountContext;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.DataResponseDto;
import com.maruhxn.boardserver.dto.response.PageItem;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.dto.response.object.PostDetailItem;
import com.maruhxn.boardserver.dto.response.object.PostItem;
import com.maruhxn.boardserver.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public DataResponseDto<PageItem<PostItem>> getPostList(
            @ModelAttribute @Valid PostSearchCond postSearchCond,
            Pageable pageable) {
        log.info("title={}, content={}, author={}",
                postSearchCond.getTitle(),
                postSearchCond.getContent(),
                postSearchCond.getAuthor());
        Page<PostItem> postList = postService.getPostList(postSearchCond, pageable);
        return DataResponseDto.ok("게시글 리스트 조회 성공", PageItem.fromPage(postList));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createPost(@AuthenticationPrincipal AccountContext accountContext, @Valid CreatePostRequest createPostRequest) {
        log.info("title={}, content={}, images={}",
                createPostRequest.getTitle(),
                createPostRequest.getContent(),
                createPostRequest.getImages());
        postService.createPost(accountContext.getMember(), createPostRequest);
        return ResponseDto.ok("게시글 생성 성공");
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public DataResponseDto<PostDetailItem> getPostDetail(@PathVariable Long postId) {
        PostDetailItem result = postService.getPostDetail(postId);
        return DataResponseDto.ok("게시글 조회 성공", result);
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long postId, UpdatePostRequest updatePostRequest) {
        log.info("게시글 수정 | title={}, content={}, images={}",
                updatePostRequest.getTitle(),
                updatePostRequest.getContent(),
                updatePostRequest.getImages());
        postService.updatePost(postId, updatePostRequest);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        log.info("게시글 삭제 | postId={}", postId);
        postService.deletePost(postId);
    }

    @DeleteMapping("/{postId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long imageId) {
        log.info("이미지 삭제 | imageId={}", imageId);
        postService.deleteImage(imageId);
    }
}
