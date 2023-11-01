package com.maruhxn.boardserver.dto.response;

import lombok.Data;

@Data
public class CommentAuthor {
    private final Long memberId;
    private final String username;
    private final String profileImage;
}
