package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAnonymous()")
    public ResponseDto register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        log.info("회원가입 = {}", registerRequest);
        return ResponseDto.ok("회원가입 성공");
    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseDto login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
//        session.setAttribute(SessionConst.LOGIN_MEMBER, authService.login(loginRequest));
//        return ResponseDto.ok("로그인 성공");
//    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }
}
