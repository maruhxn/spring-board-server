package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailItem {
    private Long postId;
    private String title;
    private String content;
    private List<PostImageItem> images;
    private AuthorItem author;
    private Long viewCount;
    private LocalDateTime createdAt;

    @Builder
    public PostDetailItem(Long postId, String title, String content, List<PostImageItem> images, AuthorItem author, Long viewCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.images = images;
        this.author = author;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
    }

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
