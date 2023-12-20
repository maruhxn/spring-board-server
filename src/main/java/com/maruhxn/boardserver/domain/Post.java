package com.maruhxn.boardserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "images", "comments"})
@DynamicInsert
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content, Member member) {
        Assert.hasText(title, "제목은 필수입니다.");
        Assert.hasText(content, "내용은 필수입니다.");
        Assert.notNull(member, "유저 정보는 필수입니다.");

        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0L;
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
