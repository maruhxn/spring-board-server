package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class BadRequestException extends GlobalException {
    public BadRequestException(ErrorCode code) {
        super(code);
    }
}
