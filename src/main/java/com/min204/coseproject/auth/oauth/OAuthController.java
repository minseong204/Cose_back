package com.min204.coseproject.auth.oauth;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OAuthController {

    @GetMapping("/loading")
    public ResponseEntity loginError(@RequestParam String refreshToken) {
        if (refreshToken.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
