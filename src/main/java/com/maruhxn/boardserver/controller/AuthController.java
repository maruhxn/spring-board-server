package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Object> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseDto.empty("회원가입 성공");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> login(@RequestBody LoginRequest loginRequest) {
        return ResponseDto.empty("로그인 성공");
    }

    @PostMapping("/{memberId}/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> confirmPassword(@RequestBody ConfirmPasswordRequest confirmPasswordRequest) {
        return ResponseDto.empty("비밀번호 인증 성공");
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        return;
    }
}
