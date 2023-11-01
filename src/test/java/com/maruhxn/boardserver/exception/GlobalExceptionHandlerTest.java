package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("핸들러 - GlobalExceptionHandler")
class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler sut;

    @BeforeEach
    void setUp() {
        sut = new GlobalExceptionHandler();
    }

    @DisplayName("@Validated 검증 오류 - 응답 데이터 정의")
    @Test
    void givenParameterValidationException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());

        // When
        ResponseEntity<Object> response = sut.paramValidation(e);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ResponseDto.error(ErrorCode.VALIDATION_ERROR.getCode(), ErrorCode.VALIDATION_ERROR.getMessage()))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("프로젝트 일반 오류 - 응답 데이터 정의")
    @Test
    void givenGlobalException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        GlobalException e = new GlobalException(errorCode);

        // When
        ResponseEntity<Object> response = sut.globalException(e);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ResponseDto.error(errorCode.getCode(), e.getMessage()))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("기타(전체) 오류 - 응답 데이터 정의")
    @Test
    void givenException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        Exception e = new Exception("예상치 못한 오류!");

        // When
        ResponseEntity<Object> response = sut.exception(e);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ResponseDto.error(ErrorCode.INTERNAL_ERROR.getCode(), "예상치 못한 오류!"))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

