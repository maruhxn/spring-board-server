package com.maruhxn.boardserver.dto.response.object;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {
    private Long memberId;
    private String email;
    private String username;
    private String profileImage;

    @Builder
    public MemberInfo(Long memberId, String email, String username, String profileImage) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
    }
}
