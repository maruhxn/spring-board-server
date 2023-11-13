package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentItem {
    private final Long commentId;
    private final String content;
    private final CommentAuthor author;

    public static CommentItem fromEntity(Comment comment) {
        return CommentItem.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(CommentAuthor.fromEntity(comment.getMember()))
                .build();
    }
}
