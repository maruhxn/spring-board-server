package com.maruhxn.boardserver.dto.response;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto {
    private final String code;
    private final String message;

    public static ResponseDto ok(String message) {
        return new ResponseDto("OK", message);
    }

    public static ResponseDto error(ErrorCode errorCode) {
        return new ResponseDto(errorCode.name(), errorCode.getMessage());
    }

    public static ResponseDto error(ErrorCode errorCode, String message) {
        return new ResponseDto(errorCode.name(), message);
    }
}
