package com.maruhxn.boardserver.dto.request.members;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class UpdatePasswordRequest {
    @Size(min = 2, max = 20, message = "비밀번호는 2 ~ 20 글자입니다.")
    private String currPassword;
    @Size(min = 2, max = 20, message = "비밀번호는 2 ~ 20 글자입니다.")
    private String newPassword;
    @Size(min = 2, max = 20, message = "비밀번호는 2 ~ 20 글자입니다.")
    private String confirmNewPassword;
}
