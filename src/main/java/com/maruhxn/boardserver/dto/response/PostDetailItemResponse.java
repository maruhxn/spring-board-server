package com.maruhxn.boardserver.dto.response;

import lombok.Data;

@Data
public class PostDetailItemResponse {
    private final Long postId;
    private final String title;
    private final String content;
    private final String authorName;
    private final String createdAt;
    private final Long viewCount;
}
