package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorItem {
    private final Long memberId;
    private final String username;
    private final String profileImage;

    public static AuthorItem fromEntity(Member member) {
        return AuthorItem.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .profileImage(member.getProfileImage())
                .build();
    }
}
