package com.maruhxn.boardserver.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> {
    private final Integer code;
    private final String message;
    private final T data;

    public static <T> ResponseDto<T> data(final T data) {
        return new ResponseDto<>(10000, "OK", data);
    }

    public static <T> ResponseDto<T> error(Integer code, String message) {
        return new ResponseDto<>(code, message, null);
    }
}
