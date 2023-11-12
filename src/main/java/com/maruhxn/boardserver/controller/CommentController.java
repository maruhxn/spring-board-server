package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.CommentItem;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/posts/{postId}/comments")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<CommentItem>> getCommentList(@PathVariable Long postId, @RequestParam(value = "page", defaultValue = "0") int page) {
        List<CommentItem> commentList = commentService.getCommentList(postId, page);
        return ResponseDto.data("댓글 리스트 조회 성공", commentList);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Object> createComment(HttpSession session, @PathVariable Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        log.info("content={}", createCommentRequest.getContent());

        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );
        commentService.createComment(member, postId, createCommentRequest);
        return ResponseDto.empty("댓글 생성 성공");
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(HttpSession session, @PathVariable Long postId, @PathVariable Long commentId) {
        log.info("postId={}, commentId={}", postId, commentId);
        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );
        commentService.deleteComment(member.getId(), commentId);
    }

}
