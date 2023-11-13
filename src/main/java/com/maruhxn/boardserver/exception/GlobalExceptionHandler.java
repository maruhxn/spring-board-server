package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> paramValidation(ConstraintViolationException e) {
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR.getCode(), ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> bodyValidation(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.VALIDATION_ERROR.getCode(), ErrorCode.VALIDATION_ERROR.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> globalException(GlobalException e) {
        return ResponseEntity
                .status(e.getCode().getHttpStatus())
                .body(ResponseDto.error(e.getCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.INTERNAL_ERROR.getCode(), e.getMessage()));
    }
}
