package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.SessionConst;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<String> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        log.info("회원가입 = {}", registerRequest);
        return ResponseDto.empty("회원가입 성공");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        session.setAttribute(SessionConst.LOGIN_MEMBER, authService.login(loginRequest));
        return ResponseDto.empty("로그인 성공");
    }

    @PostMapping("/{memberId}/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> confirmPassword(
            @RequestBody @Valid ConfirmPasswordRequest confirmPasswordRequest,
            @PathVariable Long memberId
    ) {
        authService.confirmPassword(memberId, confirmPasswordRequest);
        return ResponseDto.empty("비밀번호 인증 성공");
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
