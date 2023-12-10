package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberItem {
    private final Long memberId;
    private final String email;
    private final String username;
    private final String profileImage;
    private final Role role;
}
