package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.PostDetailItemResponse;
import com.maruhxn.boardserver.dto.response.PostItemResponse;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
@RestController
@Slf4j
public class PostController {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> getMemberDetail(@ModelAttribute PostSearchCond postSearchCond) {
        log.info("title={}, content={}, author={}, page={}", postSearchCond.getTitle(), postSearchCond.getContent(), postSearchCond.getAuthor(), postSearchCond.getPage());
        return ResponseDto.data(
                "게시글 리스트 조회 성공",
                List.of(
                        new PostItemResponse(1L, "제목", "내용", "tester", "2023-10-27T00:00:00", 1L, 3L),
                        new PostItemResponse(1L, "제목", "내용", "tester", "2023-10-27T00:00:00", 1L, 3L)
                ));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Object> createPost(@RequestBody CreatePostRequest createPostRequest) {
        log.info("title={}, content={}, images={}", createPostRequest.getTitle(), createPostRequest.getContent(), createPostRequest.getImages());
        return ResponseDto.empty("게시글 생성 성공");
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<PostDetailItemResponse> getPostDetail(@PathVariable Long postId) {
        log.info("postId={}", postId);
        return ResponseDto.data("게시글 조회 성공", new PostDetailItemResponse(
                1L,
                "제목",
                "내용",
                "tester",
                "2023-10-27T00:00:00",
                1L
        ));
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest) {
        log.info("title={}, content={}, images={}", updatePostRequest.getTitle(), updatePostRequest.getContent(), updatePostRequest.getImages());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable Long postId) {
        log.info("postId={}", postId);
        return;
    }
}
