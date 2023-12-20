package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class AlreadyExistsResourceException extends GlobalException {
    public AlreadyExistsResourceException(ErrorCode code) {
        super(code);
    }
}
