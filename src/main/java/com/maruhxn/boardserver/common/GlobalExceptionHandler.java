package com.maruhxn.boardserver.common;

import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Access Denied 핸들러
    @ExceptionHandler
    public ResponseEntity<Object> accessDenied(AccessDeniedException e) {
        return ResponseEntity
                .status(ErrorCode.FORBIDDEN.getHttpStatus())
                .body(ErrorResponseDto.error(ErrorCode.FORBIDDEN));
    }

    // 404예외처리 핸들러
    @ExceptionHandler
    public ResponseEntity<Object> handle404(NoHandlerFoundException e) {
        return ResponseEntity
                .status(ErrorCode.NOT_FOUND_RESOURCE.getHttpStatus())
                .body(ErrorResponseDto.error(ErrorCode.NOT_FOUND_RESOURCE));
    }

    @ExceptionHandler
    public ResponseEntity<Object> dataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(ErrorCode.EXISTING_USERNAME.getHttpStatus())
                .body(ErrorResponseDto.error(ErrorCode.EXISTING_USERNAME));
    }

    @ExceptionHandler
    public ResponseEntity<Object> validationFail(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(ErrorResponseDto.validationError(e.getBindingResult()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> globalException(GlobalException e) {
        return ResponseEntity
                .status(e.getCode().getHttpStatus())
                .body(ErrorResponseDto.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ErrorResponseDto.error(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

}
