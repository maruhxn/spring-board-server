package com.maruhxn.boardserver.dto.response;

import lombok.Data;

@Data
public class CommentItem {
    private final Long commentId;
    private final String content;
    private final CommentAuthor author;
}
