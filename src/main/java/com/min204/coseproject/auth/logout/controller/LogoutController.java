package com.min204.coseproject.auth.logout.controller;

import com.min204.coseproject.auth.jwt.JwtTokenizer;
import com.min204.coseproject.auth.utils.RedisUtil;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class LogoutController {
    private final JwtTokenizer jwtTokenizer;
    private final RedisUtil redisUtil;
    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") @NotBlank String token) {
        User user = userService.getLoginMember();

        String accessToken = token.replace("Bearer ", "");
        String encodeBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        log.info("access Token 남은 유효시간 : {} ", jwtTokenizer.getExpiration(accessToken, encodeBase64SecretKey));

        try {
            redisUtil.setBlackList(accessToken, "access_token", jwtTokenizer.getBlacklistTime(jwtTokenizer.getExpiration(accessToken, encodeBase64SecretKey)));
        } catch (NullPointerException exception) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_LOGIN);
        }

        if (redisUtil.hasKey(user.getEmail())) {
            redisUtil.delete(user.getEmail());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
