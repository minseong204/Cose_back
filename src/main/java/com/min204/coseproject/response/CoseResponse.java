package com.min204.coseproject.response;

import com.min204.coseproject.constant.BodyCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class CoseResponse {
    private static ResBodyModel toBody(String status, BodyCode bodyCode, Object data) {
        return ResBodyModel.builder()
                .status(status)
                .code(bodyCode.getCode())
                .message(bodyCode.getMessage())
                .data(data)
                .build();
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode) {
        return ResponseEntity.ok().body(toBody("Success", bodyCode, null));
    }

    public static ResponseEntity<ResBodyModel> toResponse(String message, BodyCode bodyCode) {
        return ResponseEntity.ok().body(toBody("Error", bodyCode, null));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, int status) {
        return ResponseEntity.status(status).body(toBody("Success", bodyCode, null));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object data) {
        return ResponseEntity.ok().body(toBody("Success", bodyCode, data));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object data, int status) {
        return ResponseEntity.status(status).body(toBody("Success", bodyCode, data));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object data, int status, HttpHeaders headers) {
        return ResponseEntity.status(status).headers(headers).body(toBody("Success", bodyCode, data));
    }

    public static ResponseEntity<ResBodyModel> toErrorResponse(BodyCode bodyCode, Object data) {
        return ResponseEntity.badRequest().body(toBody("Bad Request", bodyCode, data));
    }

    public static ResponseEntity<ResBodyModel> toErrorResponse(String message, int status) {
        return ResponseEntity.status(status).body(toBody("Error", new BodyCode() {
            @Override
            public String getCode() {
                return "ERROR";
            }

            @Override
            public String getMessage() {
                return message;
            }
        }, null));
    }
}