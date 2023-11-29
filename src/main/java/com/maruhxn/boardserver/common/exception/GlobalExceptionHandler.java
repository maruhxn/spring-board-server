package com.maruhxn.boardserver.common.exception;

import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Access Denied 핸들러
    @ExceptionHandler
    public ResponseEntity<Object> accessDenied(AccessDeniedException e) {
        return ResponseEntity
                .status(ErrorCode.FORBIDDEN.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.FORBIDDEN));
    }

    // 404예외처리 핸들러
    @ExceptionHandler
    public ResponseEntity<Object> handle404(NoHandlerFoundException e) {
        return ResponseEntity
                .status(ErrorCode.NOT_FOUND_RESOURCE.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.NOT_FOUND_RESOURCE));
    }

    @ExceptionHandler
    public ResponseEntity<Object> dataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(ErrorCode.EXISTING_USERNAME.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.EXISTING_USERNAME));
    }

    @ExceptionHandler
    public ResponseEntity<Object> validationFail(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(new ValidationFailedResponseDto(ErrorCode.VALIDATION_ERROR.name(), errors));
    }

    @ExceptionHandler
    public ResponseEntity<Object> globalException(GlobalException e) {
        return ResponseEntity
                .status(e.getCode().getHttpStatus())
                .body(ResponseDto.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ResponseDto.error(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

    @Data
    static private class ValidationFailedResponseDto {
        private final String code;
        private final List<String> messages;
    }
}
