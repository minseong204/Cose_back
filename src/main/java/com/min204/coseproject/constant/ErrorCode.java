package com.min204.coseproject.constant;

import lombok.Getter;

@Getter
public enum ErrorCode implements BodyCode {
    EMAIL_EXISTS("400 BAD REQUEST", "이미 사용 중인 이메일입니다."),
    FAILED_LOGIN("401 UNAUTHORIZED", "로그인에 실패하였습니다.")
    ;
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
