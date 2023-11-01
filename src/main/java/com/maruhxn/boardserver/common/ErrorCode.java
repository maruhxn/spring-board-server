package com.maruhxn.boardserver.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "스프링 BAD REQUEST"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "올바르지 않은 입력입니다."),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "요청하신 URL 혹은 자원이 존재하지 않습니다."),
    UNAUTHORIZED(10004, HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    FORBIDDEN(10005, HttpStatus.FORBIDDEN, "권한이 없습니다."),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
    SPRING_INTERNAL_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "스프링 오류 입니다."),
    DATA_ACCESS_ERROR(20002, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류입니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

}
