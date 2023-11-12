package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostItemResponse {
    private final Long postId;
    private final String title;
    private final String content;
    private final String authorName;
    private final LocalDateTime createdAt;
    private final Long viewCount;
    private final Long commentCount;

    public static PostItemResponse fromEntity(Post p) {
        return PostItemResponse.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .authorName(p.getMember().getUsername())
                .createdAt(p.getCreatedAt())
                .viewCount(p.getViewCount())
                .commentCount(100L) // TODO: 게시글 댓글 수 조회 기능 추가
                .build();
    }
}
