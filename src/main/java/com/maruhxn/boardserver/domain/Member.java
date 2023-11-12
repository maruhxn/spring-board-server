package com.maruhxn.boardserver.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "username"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 10, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String profileImage;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public Member(String email, String username, String password, String profileImage) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
    }

    // Update Logic //

    /**
     * 회원 정보 수정 (유저명, 프로필 이미지)
     *
     * @param username
     * @param profileImage
     */
    public void updateProfile(String username, String profileImage) {
        if (StringUtils.hasText(username)) this.username = username;
        if (StringUtils.hasText(profileImage)) this.profileImage = profileImage;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
