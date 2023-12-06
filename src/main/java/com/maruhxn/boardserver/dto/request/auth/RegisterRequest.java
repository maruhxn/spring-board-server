package com.maruhxn.boardserver.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email(message = "이메일 형식에 맞추어 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "유저명을 입력해주세요.")
    @Size(min = 2, max = 10, message = "유저명은 2 ~ 10 글자입니다.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 2, max = 20, message = "비밀번호는 2 ~ 20 글자입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인값을 입력해주세요.")
    @Size(min = 2, max = 20, message = "비밀번호 확인값은 2 ~ 20 글자입니다.")
    private String confirmPassword;
}
