package com.maruhxn.boardserver.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.boardserver.auth.common.AjaxAuthenticationToken;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Set;

public class AjaxLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;

    public AjaxLoginFilter() {
        super(new AntPathRequestMatcher("/auth/login")); // 이 URL일 때만 요청 처리
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isAjax(request) && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(ErrorCode.BAD_REQUEST.getMessage());
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        Set<ConstraintViolation<LoginRequest>> validate = validator.validate(loginRequest);
        if (!validate.isEmpty()) {
            throw new AuthenticationServiceException("유효하지 않은 로그인 요청입니다.");
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
