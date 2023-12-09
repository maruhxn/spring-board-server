package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailItem {
    private final Long postId;
    private final String title;
    private final String content;
    private final List<PostImageItem> images;
    private final AuthorItem author;
    private final Long viewCount;
    private final LocalDateTime createdAt;

    public static PostDetailItem fromEntity(Post post) {
        return PostDetailItem.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages().stream()
                        .map(PostImageItem::fromEntity)
                        .toList())
                .author(AuthorItem.fromEntity(post.getMember()))
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
