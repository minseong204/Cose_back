package com.min204.coseproject.response;

import com.min204.coseproject.constant.BodyCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoseResponse {
    private static ResBodyModel toBody(BodyCode bodyCode) {
        return ResBodyModel.builder()
                .code(bodyCode.getCode())
                .description(bodyCode.getMessage())
                .dateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                .data(null)
                .build();
    }

    private static ResBodyModel toBody(BodyCode bodyCode, Object data) {
        return ResBodyModel.builder()
                .code(bodyCode.getCode())
                .description(bodyCode.getMessage())
                .dateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                .data(data)
                .build();
    }

    private static ResBodyModel toBody(String message) {
        return ResBodyModel.builder()
                .code("ERROR")
                .description(message)
                .dateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                .data(null)
                .build();
    }

    private static ResBodyModel toBody(String message, BodyCode bodyCode) {
        return ResBodyModel.builder()
                .code(bodyCode.getCode())
                .description(message)
                .dateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                .data(null)
                .build();
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode) {
        return ResponseEntity.ok().body(toBody(bodyCode));
    }

    public static ResponseEntity<ResBodyModel> toResponse(String message, BodyCode bodyCode) {
        return ResponseEntity.ok().body(toBody(message, bodyCode));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, int status) {
        return ResponseEntity.status(status).body(toBody(bodyCode));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object body) {
        return ResponseEntity.ok().body(toBody(bodyCode, body));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object body, int status) {
        return ResponseEntity.status(status).body(toBody(bodyCode, body));
    }

    public static ResponseEntity<ResBodyModel> toResponse(BodyCode bodyCode, Object body, int status, HttpHeaders headers) {
        return ResponseEntity.status(status).headers(headers).body(toBody(bodyCode, body));
    }

    public static ResponseEntity<ResBodyModel> toErrorResponse(String message, int status) {
        return ResponseEntity.status(status).body(toBody(message));
    }
}