package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorItem {
    private Long memberId;
    private String username;
    private String profileImage;

    @Builder
    public AuthorItem(Long memberId, String username, String profileImage) {
        this.memberId = memberId;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static AuthorItem fromEntity(Member member) {
        return AuthorItem.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .profileImage(member.getProfileImage())
                .build();
    }
}
