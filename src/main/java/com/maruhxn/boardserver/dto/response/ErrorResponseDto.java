package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.dto.response.object.FieldError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.BindingResult;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ErrorResponseDto extends ResponseDto {
    private List<FieldError> errors;

    public ErrorResponseDto(ErrorCode errorCode) {
        super(errorCode.name(), errorCode.getMessage());
        this.errors = List.of();
    }

    public ErrorResponseDto(ErrorCode errorCode, String message) {
        super(errorCode.name(), message);
        this.errors = List.of();
    }

    public ErrorResponseDto(ErrorCode errorCode, List<FieldError> errors) {
        super(errorCode.name(), errorCode.getMessage());
        this.errors = errors;
    }

    public static ErrorResponseDto error(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto error(ErrorCode errorCode, String message) {
        return new ErrorResponseDto(errorCode, message);
    }

    public static ErrorResponseDto validationError(BindingResult bindingResult) {
        return new ErrorResponseDto(ErrorCode.VALIDATION_ERROR, FieldError.of(bindingResult));
    }

}
