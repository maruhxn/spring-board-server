package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.CommentItem;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.resolver.Login;
import com.maruhxn.boardserver.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts/{postId}/comments")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<CommentItem>> getCommentList(
            @PathVariable Long postId,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        List<CommentItem> commentList = commentService.getCommentList(postId, page);
        return ResponseDto.data("댓글 리스트 조회 성공", commentList);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Object> createComment(@Login Member loginMember, @PathVariable Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        log.info("content={}", createCommentRequest.getContent());
        commentService.createComment(loginMember, postId, createCommentRequest);
        return ResponseDto.empty("댓글 생성 성공");
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Login Member loginMember, @PathVariable Long postId, @PathVariable Long commentId) {
        log.info("postId={}, commentId={}", postId, commentId);
        commentService.deleteComment(loginMember.getId(), commentId);
    }

}
