package com.maruhxn.boardserver.dto.request.members;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberProfileRequest {
    @Size(min = 2, max = 10)
    private String username;
    private String profileImage;
}
