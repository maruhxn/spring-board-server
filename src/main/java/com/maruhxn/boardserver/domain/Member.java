package com.maruhxn.boardserver.domain;


import com.mysema.commons.lang.Assert;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Entity
@Table(
        indexes = {
                @Index(name = "idx__email__username", columnList = "email, username")
        }) // (email), (email, username) cover
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@DynamicInsert
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 10, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role;

    @Builder
    public Member(String email, String username, String password, String profileImage, Role role) {
        Assert.hasText(email, "이메일은 필수입니다.");
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.role = role;
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
