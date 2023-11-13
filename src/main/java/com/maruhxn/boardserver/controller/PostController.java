package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.PostDetailItemResponse;
import com.maruhxn.boardserver.dto.response.PostItemResponse;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.resolver.Login;
import com.maruhxn.boardserver.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<PostItemResponse>> getMemberDetail(@ModelAttribute PostSearchCond postSearchCond) {
        log.info("title={}, content={}, author={}, page={}",
                postSearchCond.getTitle(),
                postSearchCond.getContent(),
                postSearchCond.getAuthor(),
                postSearchCond.getPage());
        List<PostItemResponse> result = postService.getPostList(postSearchCond);
        return ResponseDto.data("게시글 리스트 조회 성공", result);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<String> createPost(@Login Member loginMember, @Valid CreatePostRequest createPostRequest) {
        log.info("title={}, content={}, images={}",
                createPostRequest.getTitle(),
                createPostRequest.getContent(),
                createPostRequest.getImages());
        postService.createPost(loginMember, createPostRequest);
        return ResponseDto.empty("게시글 생성 성공");
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<PostDetailItemResponse> getPostDetail(@PathVariable Long postId) {
        PostDetailItemResponse result = postService.getPostDetail(postId);
        return ResponseDto.data("게시글 조회 성공", result);
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long postId,
                           @Login Member loginMember,
                           UpdatePostRequest updatePostRequest) {
        log.info("게시글 수정 | title={}, content={}, images={}",
                updatePostRequest.getTitle(),
                updatePostRequest.getContent(),
                updatePostRequest.getImages());
        postService.updatePost(postId, updatePostRequest);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId, @Login Member loginMember) {
        log.info("게시글 삭제 | postId={}", postId);
        postService.deletePost(loginMember.getId(), postId);
    }

    @DeleteMapping("/{postId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@Login Member loginMember, @PathVariable Long imageId) {
        log.info("이미지 삭제 | imageId={}", imageId);
        postService.deleteImage(loginMember.getId(), imageId);
    }
}
