package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailItemResponse {
    private final Long postId;
    private final String title;
    private final String content;
    private final List<PostImageItem> images;
    private final String authorName;
    private final Long viewCount;
    private final LocalDateTime createdAt;

    public static PostDetailItemResponse fromEntity(Post post) {
        return PostDetailItemResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages().stream()
                        .map(PostImageItem::fromEntity)
                        .toList())
                .authorName(post.getMember().getUsername())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
