package com.maruhxn.boardserver.dto.request.members;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class UpdateMemberProfileRequest {
    @Size(min = 2, max = 10, message = "유저명은 2 ~ 10 글자입니다.")
    private String username;
    private String profileImage;
}
