package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.PostDetailItemResponse;
import com.maruhxn.boardserver.dto.response.PostItemResponse;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/posts")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<PostItemResponse>> getMemberDetail(@ModelAttribute PostSearchCond postSearchCond) {
        log.info("title={}, content={}, author={}, page={}", postSearchCond.getTitle(), postSearchCond.getContent(), postSearchCond.getAuthor(), postSearchCond.getPage());
        List<PostItemResponse> result = postService.getPostList(postSearchCond);
        return ResponseDto.data("게시글 리스트 조회 성공", result);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<String> createPost(HttpSession session, @Valid CreatePostRequest createPostRequest) {
        log.info("title={}, content={}, images={}", createPostRequest.getTitle(), createPostRequest.getContent(), createPostRequest.getImages());

        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );

        postService.createPost(member, createPostRequest);
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
    public void updatePost(@PathVariable Long postId, HttpSession session, UpdatePostRequest updatePostRequest) {
        log.info("게시글 수정 | title={}, content={}, images={}", updatePostRequest.getTitle(), updatePostRequest.getContent(), updatePostRequest.getImages());
        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );
        postService.updatePost(member.getId(), postId, updatePostRequest);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId, HttpSession session) {
        log.info("게시글 삭제 | postId={}", postId);
        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );
        postService.deletePost(member.getId(), postId);
    }


}
