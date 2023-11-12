package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Comment;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.CommentItem;
import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.repository.CommentRepository;
import com.maruhxn.boardserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(Member member, Long postId, CreateCommentRequest createCommentRequest) {

        Post findPost = postRepository.findOne(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "게시글 정보가 없습니다."));

        Comment comment = Comment.builder()
                .content(createCommentRequest.getContent())
                .member(member)
                .post(findPost)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentItem> getCommentList(Long postId, int page) {
        List<Comment> comments = commentRepository.findAll(postId, page);
        return comments.stream()
                .map(CommentItem::fromEntity)
                .toList();
    }

    public void deleteComment(Long memberId, Long commentId) {
        Comment findComment = commentRepository.findOne(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "댓글 정보가 없습니다."));
        checkIsAuthor(memberId, findComment);

        commentRepository.removeOne(findComment);
    }

    private static void checkIsAuthor(Long memberId, Comment comment) {
        Long authorId = comment.getMember().getId();
        if (!authorId.equals(memberId)) throw new GlobalException(ErrorCode.FORBIDDEN);
    }
}
