package com.min204.coseproject.exception;

import com.min204.coseproject.constant.ErrorCode;
import lombok.Getter;

public class BusinessLogicException extends RuntimeException{
    @Getter
    private ErrorCode errorCode;

    public BusinessLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
