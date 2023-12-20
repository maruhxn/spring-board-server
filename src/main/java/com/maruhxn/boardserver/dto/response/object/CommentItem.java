package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentItem {
    private Long commentId;
    private String content;
    private AuthorItem author;
    private LocalDateTime createdAt;

    @Builder
    public CommentItem(Long commentId, String content, AuthorItem author, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static CommentItem fromEntity(Comment comment) {
        return CommentItem.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(AuthorItem.fromEntity(comment.getMember()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
