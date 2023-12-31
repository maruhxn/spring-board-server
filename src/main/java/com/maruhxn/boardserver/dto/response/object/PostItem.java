package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostItem {
    private Long postId;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long commentCount;

    @Builder
    @QueryProjection
    public PostItem(Long postId, String title, String authorName, LocalDateTime createdAt, Long viewCount, Long commentCount) {
        this.postId = postId;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }

    public static PostItem fromEntity(Post p) {
        return PostItem.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .authorName(p.getMember().getUsername())
                .createdAt(p.getCreatedAt())
                .viewCount(p.getViewCount())
                .commentCount((long) p.getComments().size())
                .build();
    }
}
