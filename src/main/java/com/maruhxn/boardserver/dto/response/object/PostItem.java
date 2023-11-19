package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public PostItem(Long postId, String title, String content, String authorName, LocalDateTime createdAt, Long viewCount, Long commentCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public static PostItem fromEntity(Post p) {
        return PostItem.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .authorName(p.getMember().getUsername())
                .createdAt(p.getCreatedAt())
                .viewCount(p.getViewCount())
                .commentCount((long) p.getComments().size())
                .build();
    }
}
