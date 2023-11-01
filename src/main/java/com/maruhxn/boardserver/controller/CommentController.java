package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.CommentAuthor;
import com.maruhxn.boardserver.dto.response.CommentItem;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts/{postId}/comments")
@RestController
@Slf4j
public class CommentController {
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> getCommentList(@PathVariable Long postId) {
        log.info("postId={}", postId);
        return ResponseDto.data(
                "댓글 리스트 조회 성공",
                List.of(
                        new CommentItem(1L, "댓글 내용1", new CommentAuthor(1L, "tester", "default-profile-image-path")),
                        new CommentItem(2L, "댓글 내용2", new CommentAuthor(1L, "tester", "default-profile-image-path"))
                )
        );
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Object> createComment(@PathVariable Long postId, @RequestBody CreateCommentRequest createCommentRequest) {
        log.info("content={}", createCommentRequest.getContent());
        return ResponseDto.empty("댓글 생성 성공");
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        log.info("postId={}, commentId={}", postId, commentId);
        return;
    }

}
