package com.maruhxn.boardserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* BAD REQUEST 400 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),
    PASSWORD_CONFIRM_FAIL(HttpStatus.BAD_REQUEST, "'비밀번호'와 '비밀번호 확인'이 일치하지 않습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "동일한 비밀번호로 변경할 수 없습니다."),
    SPRING_BAD_REQUEST(HttpStatus.BAD_REQUEST, "스프링 BAD REQUEST"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "올바르지 않은 입력입니다."),
    /* UNAUTHORIZED 401 */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    /* FORBIDDEN 403 */
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    /* NOT FOUND 404 */
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "요청하신 URL 혹은 자원이 존재하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글 정보가 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글 정보가 존재하지 않습니다."),
    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "이미지 정보가 존재하지 않습니다."),
    /* UNPROCESSABLE CONTENT 422 */
    EXISTING_USER(HttpStatus.UNPROCESSABLE_ENTITY, "이미 존재하는 이메일 혹은 유저명입니다."),
    LOGOUT_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "이미 로그인 되어있습니다."),

    /* INTERNAL SERVER ERROR  500 */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
    SPRING_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "스프링 오류 입니다."),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
