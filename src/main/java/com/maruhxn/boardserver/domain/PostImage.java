package com.maruhxn.boardserver.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public PostImage(String imageName, String imagePath, Post post) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.post = post;
    }
}
