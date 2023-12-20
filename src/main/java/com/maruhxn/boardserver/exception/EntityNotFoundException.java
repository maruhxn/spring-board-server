package com.maruhxn.boardserver.exception;

import com.maruhxn.boardserver.common.ErrorCode;

public class EntityNotFoundException extends GlobalException {
    public EntityNotFoundException(ErrorCode code) {
        super(code);
    }
}
