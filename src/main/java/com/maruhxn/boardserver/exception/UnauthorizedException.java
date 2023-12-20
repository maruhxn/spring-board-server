package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class UnauthorizedException extends GlobalException {
    public UnauthorizedException(ErrorCode code) {
        super(code);
    }
}
