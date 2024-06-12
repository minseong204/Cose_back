package com.min204.coseproject.constant;

import lombok.Getter;

@Getter
public enum SuccessCode implements BodyCode {
    SUCCESS( "성공!", "Done"),
    SIGN_UP_SUCCESS("201CREATED", "회원가입성공"),
    LOGIN_SUCCESS("202 ACCEPTED", "로그인 성공"),
    REISSUE_SUCCESS("200 SUCCESS", "재발행 성공"),
    EMAIL_AVAILABLE("200 SUCCESS", "이메일 사용가능")
    ;
    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
