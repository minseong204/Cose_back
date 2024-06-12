package com.min204.coseproject.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Response Body 기본 베이스 코드이다.
 */
@Getter
public class ResBodyModel {
    private final String status;
    private final String code;
    private final String message;
    private final Object data;

    @Builder
    public ResBodyModel(String status, String code, String message, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}