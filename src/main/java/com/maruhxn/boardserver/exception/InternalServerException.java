package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class InternalServerException extends GlobalException {


    public InternalServerException(ErrorCode code) {
        super(code);
    }

    public InternalServerException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InternalServerException(ErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
