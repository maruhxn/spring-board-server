package com.maruhxn.boardserver.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.dto.response.ErrorResponseDto;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseDto responseDto = null;

        if (exception instanceof BadCredentialsException) {
            responseDto = ErrorResponseDto.error(ErrorCode.INCORRECT_PASSWORD);
        } else if (exception instanceof UsernameNotFoundException) {
            responseDto = ErrorResponseDto.error(ErrorCode.NOT_FOUND_USER);
        }

        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
