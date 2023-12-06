package com.maruhxn.boardserver.dto.response;

import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {
    private final T data;

    protected DataResponseDto(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> DataResponseDto<T> ok(String message, T data) {
        return new DataResponseDto<>("OK", message, data);
    }
}
