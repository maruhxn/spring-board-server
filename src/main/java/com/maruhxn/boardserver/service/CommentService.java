package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Comment;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.dto.request.comments.CreateCommentRequest;
import com.maruhxn.boardserver.dto.response.object.CommentItem;
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

        Post findPost = postRepository.findOneWithAuthorAndImages(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_POST));

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

    public void deleteComment(Member loginMember, Long commentId) {
        Comment findComment = commentRepository.findOne(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_COMMENT));
        checkIsAuthor(loginMember.getId(), findComment);

        commentRepository.removeOne(findComment);
    }

    private static void checkIsAuthor(Long memberId, Comment comment) {
        Long authorId = comment.getMember().getId();
        if (!authorId.equals(memberId)) throw new GlobalException(ErrorCode.FORBIDDEN);
    }
}
