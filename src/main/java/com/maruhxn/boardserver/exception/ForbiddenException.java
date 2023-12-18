package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class ForbiddenException extends GlobalException {
    public ForbiddenException(ErrorCode code) {
        super(code);
    }
}
