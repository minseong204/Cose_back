package com.min204.coseproject.auth.controller;

import com.min204.coseproject.auth.dto.AuthEmailRequestDto;
import com.min204.coseproject.auth.service.AuthEmailService;
import com.min204.coseproject.response.ResBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/email")
public class AuthEmailController {
    private final AuthEmailService authEmailService;

    @GetMapping("/{email_addr}/authcode")
    public ResponseEntity<String> sendEmailPath(@PathVariable String email_addr) throws MessagingException {
        authEmailService.sendEmail(email_addr);
        return ResponseEntity.ok("이메일을 확인하세요");
    }

    @PostMapping("/{email_address}/authcode")
    public ResponseEntity<String> sendEmailAndCode(@PathVariable String email_address, @RequestBody AuthEmailRequestDto dto)
            throws NoSuchAlgorithmException {
        if (authEmailService.verifyEmailCode(email_address, dto.getCode())) {
            return ResponseEntity.ok("회원가입 가능함");
        }
        return ResponseEntity.ok("잘못됌");
    }
}
