package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentItem {
    private final Long commentId;
    private final String content;
    private final CommentAuthor author;
    private final LocalDateTime createdAt;

    public static CommentItem fromEntity(Comment comment) {
        return CommentItem.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(CommentAuthor.fromEntity(comment.getMember()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
