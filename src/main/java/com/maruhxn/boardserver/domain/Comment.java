package com.maruhxn.boardserver.domain;

import com.mysema.commons.lang.Assert;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"member", "post"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(String content, Member member, Post post) {
        Assert.hasText(content, "내용은 필수입니다.");
        Assert.notNull(member, "유저 정보는 필수입니다.");
        Assert.notNull(post, "게시글 정보는 필수입니다.");

        this.content = content;
        this.member = member;
        this.post = post;
    }
}
