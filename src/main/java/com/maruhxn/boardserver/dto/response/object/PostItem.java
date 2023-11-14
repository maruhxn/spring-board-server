package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostItem {
    private final Long postId;
    private final String title;
    private final String content;
    private final String authorName;
    private final LocalDateTime createdAt;
    private final Long viewCount;
    private final Long commentCount;

    public static PostItem fromEntity(Post p) {
        return PostItem.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .authorName(p.getMember().getUsername())
                .createdAt(p.getCreatedAt())
                .viewCount(p.getViewCount())
                .commentCount((long) p.getComments().size()) // TODO: 게시글 댓글 수 조회 기능 추가
                .build();
    }
}
