package com.maruhxn.boardserver.dto.request.members;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberProfileRequest {
    @Size(min = 2, max = 10, message = "유저명은 2 ~ 10 글자입니다.")
    private String username;
    private MultipartFile profileImage;

    @Builder
    public UpdateMemberProfileRequest(String username, MultipartFile profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}
