package com.min204.coseproject.exception;


import com.min204.coseproject.constant.LoginType;

public class EmailAlreadyExistsException extends RuntimeException {
    private final String email;
    private final LoginType loginType;

    public EmailAlreadyExistsException(String email, LoginType loginType) {
        super(String.format("Email %s already exists", email));
        this.email = email;
        this.loginType = loginType;
    }

    public String getEmail() {
        return email;
    }

    public LoginType getLoginType() {
        return loginType;
    }
}