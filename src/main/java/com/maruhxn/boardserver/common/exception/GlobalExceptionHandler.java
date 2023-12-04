package com.maruhxn.boardserver.common.exception;

import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
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

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(ValidationFailedResponse.of(ErrorCode.VALIDATION_ERROR, e.getBindingResult()));
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
    static private class ValidationFailedResponse {
        private final String code;
        private final String message;
        private final List<FieldError> errors;

        public ValidationFailedResponse(final ErrorCode code, final List<FieldError> errors) {
            this.message = code.getMessage();
            this.errors = errors;
            this.code = code.name();
        }

        public static ValidationFailedResponse of(final ErrorCode code, final BindingResult bindingResult) {
            return new ValidationFailedResponse(code, FieldError.of(bindingResult));
        }
    }
}
