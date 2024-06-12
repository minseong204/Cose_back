package com.min204.coseproject.constant;

import lombok.Getter;

@Getter
public enum SuccessCode implements BodyCode {
    SUCCESS( "성공!", "Done"),
    SIGN_UP_SUCCESS("201 CREATED", "회원가입 성공"),
    LOGIN_SUCCESS("202 ACCEPTED", "로그인 성공"),
    REISSUE_SUCCESS("200 OK", "토큰 재발행 성공"),
    EMAIL_AVAILABLE("200 OK", "이메일 사용 가능"),
    FOLLOW_SUCCESS("200 OK", "팔로우 성공"),
    UNFOLLOW_SUCCESS("200 OK", "언팔로우 성공"),
    FETCH_SUCCESS("206 PARTIAL_CONTENT", "정보 조회 성공")
    ;
    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
