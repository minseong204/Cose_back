package com.min204.coseproject.exception;

import lombok.Getter;

public enum ExceptionCode {

    USER_NOT_FOUND(404, "User Not Found"),
    CONTENT_NOT_FOUND(404, "Content Not Found"),
    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    COURSE_NOT_FOUND(404, "Course Not Found"),
    COURSE_PLACE_NOT_FOUND(404, "Course Place Not Found"),
    HEART_NOT_FOUND(404, "Heart Not Found"),

    USER_EXISTS(409, "User Exists"),
    CONTENT_EXISTS(409, "Content Exists"),
    COMMENT_EXISTS(409, "Comment Exists"),
    COURSE_EXISTS(409, "Course Exists"),
    COURSE_PLACE_EXISTS(409, "Course Place Exists"),

    CANNOT_CHANGE_USER(403, "User Can Not Change"),
    CANNOT_CHANGE_CONTENT(403, "Content Can Not Change"),
    CANNOT_CHANGE_COMMENT(403, "Comment Can Not Change"),
    CANNOT_CHANGE_COURSE(403, "Course Can Not Change"),
    CANNOT_CHANGE_COURSE_PLACE(403, "Place Can Not Change"),

    NOT_IMPLEMENTATION(501, "Not Implementation"),
    USER_NOT_LOGIN(400, "User Not Login"),
    INVALID_USER_STATUS(400, "Invalid User Status"),
    INVALID_VALUES(400, "Invalid Values"),
    INVALID_ACCESS_TOKEN(400,"권한 정보 없는 토큰"),
    INVALID_REFRESH_TOKEN(400, "Invalid Refresh_Token"),

    INVALID_EMAIL("유효하지 않은 이메일입니다."),
    UNAUTHORIZED(401, "Unautorized"),

    ALREADY_FOLLOWING(409, "이미 팔로우 되어있는 상대입니다.");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }

    ExceptionCode(String message) {
        this.message = message;
    }
}
