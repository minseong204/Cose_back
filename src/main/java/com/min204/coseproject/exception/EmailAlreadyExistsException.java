package com.min204.coseproject.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends RuntimeException {
    private final String email;
    private final String type;;

    public EmailAlreadyExistsException(String email, String type) {
        super("이미 사용중인 이메일입니다.");
        this.email = email;
        this.type = type;
    }
}
