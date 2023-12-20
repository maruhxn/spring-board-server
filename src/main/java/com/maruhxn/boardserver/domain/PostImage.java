package com.maruhxn.boardserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"post"})
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public PostImage(String originalName, String storedName) {
        Assert.hasText(originalName, "원본 이름은 필수입니다.");
        Assert.hasText(storedName, "저장된 이름은 필수입니다.");

        this.originalName = originalName;
        this.storedName = storedName;
    }

    // 편의 메서드 //
    public void setPost(Post post) {
        this.post = post;
    }
}
