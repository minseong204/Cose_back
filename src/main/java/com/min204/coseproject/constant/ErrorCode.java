package com.min204.coseproject.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BodyCode {
    /*
     * 208 Already
     * */
    SCRAP_ALREADY_EXISTS("208 Already Reported", "이미 스크랩 한 게시물 입니다."),
    ALREADY_FOLLOWING("208 Already Reported", "이미 팔로잉 상태입니다."),

    /*
     * 400 Bad Request
     * */
    EMAIL_EXISTS("400 Bad Request", "이미 사용 중인 이메일입니다."),

    /*
     * 401 UnAuthorized
     * */
    FAILED_LOGIN("401 Failed Login", "로그인에 실패하였습니다."),
    UNAUTHORIZED("401 UnAuthorized", "유효하지 않은 AccessToken 입니다."),

    /*
     * 404 Not Found
     * */
    COURSE_NOT_FOUND("404 Not Found", "코스를 찾을 수 없습니다."),
    SCRAP_NOT_FOUND("404 Not Found", "해당 스크랩을 찾을 수 없습니다."),
    CONTENT_NOT_FOUND("404 Not Found", "해당 게시물을 찾을 수 없습니다."),
    USER_NOT_FOUND("404 Not Found", "회원을 찾을 수 없습니다."),
    FOLLOW_NOT_FOUND("404 Not Found", "해당 회원이 존재하지 않거나, 팔로우 상대가 아닙니다"),

    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
