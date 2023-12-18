package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Comment;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.Role;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.object.CommentItem;
import com.maruhxn.boardserver.exception.EntityNotFoundException;
import com.maruhxn.boardserver.exception.ForbiddenException;
import com.maruhxn.boardserver.repository.CommentRepository;
import com.maruhxn.boardserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(Member member, Long postId, CreateCommentRequest createCommentRequest) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST));

        Comment comment = Comment.builder()
                .content(createCommentRequest.getContent())
                .member(member)
                .post(findPost)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentItem> getCommentList(Long postId, Pageable pageable) {
        Page<Comment> page = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId, pageable);
        return page.map(CommentItem::fromEntity);
    }

    public void deleteComment(Member loginMember, Long commentId) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_COMMENT));
        checkIsAuthorOrAdmin(loginMember, findComment);

        commentRepository.delete(findComment);
    }

    private static void checkIsAuthorOrAdmin(Member member, Comment comment) {
        Long authorId = comment.getMember().getId();
        if (!authorId.equals(member.getId()) && !member.getRole().equals(Role.ROLE_ADMIN))
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
    }
}
