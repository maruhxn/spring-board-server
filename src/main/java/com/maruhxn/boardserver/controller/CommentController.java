package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.DataResponseDto;
import com.maruhxn.boardserver.dto.response.PageItem;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.dto.response.object.CommentItem;
import com.maruhxn.boardserver.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts/{postId}/comments")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public DataResponseDto<PageItem<CommentItem>> getCommentList(
            @PathVariable Long postId,
            Pageable pageable) {
        Page<CommentItem> commentList = commentService.getCommentList(postId, pageable);
        return DataResponseDto.ok("댓글 리스트 조회 성공", PageItem.fromPage(commentList));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createComment(@AuthenticationPrincipal Member member, @PathVariable Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        log.info("content={}", createCommentRequest.getContent());
        commentService.createComment(member, postId, createCommentRequest);
        return ResponseDto.ok("댓글 생성 성공");
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("(principal.id == #member.id) or hasRole('ROLE_ADMIN')")
    public void deleteComment(@AuthenticationPrincipal Member member, @PathVariable Long postId, @PathVariable Long commentId) {
        log.info("postId={}, commentId={}", postId, commentId);
        commentService.deleteComment(member, commentId);
    }

}
