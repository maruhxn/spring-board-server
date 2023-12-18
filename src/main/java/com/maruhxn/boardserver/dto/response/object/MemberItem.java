package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberItem {
    private Long memberId;
    private String email;
    private String username;
    private String profileImage;
    private Role role;

    @Builder
    public MemberItem(Long memberId, String email, String username, String profileImage, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
        this.role = role;
    }
}
