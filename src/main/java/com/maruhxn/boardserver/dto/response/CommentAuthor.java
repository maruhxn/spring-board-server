package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentAuthor {
    private final Long memberId;
    private final String username;
    private final String profileImage;

    public static CommentAuthor fromEntity(Member member) {
        return CommentAuthor.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .profileImage(member.getProfileImage())
                .build();
    }
}
