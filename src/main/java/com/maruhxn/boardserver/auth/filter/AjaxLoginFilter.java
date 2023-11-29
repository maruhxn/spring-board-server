package com.maruhxn.boardserver.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.boardserver.auth.common.AjaxAuthenticationToken;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class AjaxLoginFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginFilter() {
        super(new AntPathRequestMatcher("/auth/login")); // 이 URL일 때만 요청 처리
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isAjax(request) && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(ErrorCode.BAD_REQUEST.getMessage());
        }

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        if (!StringUtils.hasText(loginRequest.getEmail()) || !StringUtils.hasText(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("이메일 또는 패스워드는 비어있을 수 없습니다.");
        }

        AjaxAuthenticationToken authRequest = AjaxAuthenticationToken.unauthenticated(loginRequest.getEmail(),
                loginRequest.getPassword());
//        // Allow subclasses to set the "details" property
//        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private boolean isAjax(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }

    protected void setDetails(HttpServletRequest request, AjaxAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
