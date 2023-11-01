package com.maruhxn.boardserver.dto.request.members;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberProfileRequest {
    private String username;
    private String profileImage;
}
