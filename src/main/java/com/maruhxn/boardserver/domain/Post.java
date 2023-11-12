package com.maruhxn.boardserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "images", "comments"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public Post(String title, String content, Long viewCount, Member member) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
    }

    // 연관관계 메서드 //
    public void addPostImage(PostImage postImage) {
        postImage.setPost(this);
        images.add(postImage);
    }

    // 수정 메서드 //
    public void updatePost(String title, String content, List<PostImage> images) {
        if (StringUtils.hasText(title)) this.title = title;
        if (StringUtils.hasText(content)) this.content = content;
        if (!images.isEmpty()) {
            for (PostImage image : images) {
                addPostImage(image);
            }
        }
    }

    public void addViewCount() {
        viewCount += 1;
    }

}
