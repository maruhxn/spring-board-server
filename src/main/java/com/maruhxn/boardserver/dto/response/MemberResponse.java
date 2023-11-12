package com.maruhxn.boardserver.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String username;
    private final String profileImage;
}
