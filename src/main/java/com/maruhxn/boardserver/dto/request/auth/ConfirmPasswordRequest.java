package com.maruhxn.boardserver.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfirmPasswordRequest {
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 2, max = 20, message = "비밀번호는 2 ~ 20 글자입니다.")
    private String currPassword;
}
