package com.maruhxn.boardserver.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final ErrorCode code;

    public GlobalException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public GlobalException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public GlobalException(ErrorCode code, Throwable cause) {
        super(cause.getMessage(), cause);
        this.code = code;
    }
}
