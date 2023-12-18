package com.maruhxn.boardserver.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.dto.response.ErrorResponseDto;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseDto responseDto = ErrorResponseDto.error(ErrorCode.UNAUTHORIZED);

        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
