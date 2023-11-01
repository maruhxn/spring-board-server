package com.maruhxn.boardserver.dto.response;

import lombok.Data;

@Data
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String username;
    private final String profileImage;
}
