package com.min204.coseproject.constant;

import lombok.Getter;

@Getter
public enum SuccessCode implements BodyCode {
    SUCCESS( "PA01", "Done")
    ;
    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
