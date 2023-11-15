package com.maruhxn.boardserver.common.exception;

import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

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
