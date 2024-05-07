package com.min204.coseproject.auth.controller;

import com.min204.coseproject.auth.dto.AuthLoginRequestDto;
import com.min204.coseproject.auth.dto.AuthSigUpRequestDto;
import com.min204.coseproject.auth.service.AuthService;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import com.min204.coseproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResBodyModel> signUp(@RequestBody AuthSigUpRequestDto authSigUpRequestDto) {
        User savedUser = authService.save(authSigUpRequestDto);
        log.info("savedUser = {}", savedUser);
        return CoseResponse.toResponse(SuccessCode.SUCCESS);
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody AuthLoginRequestDto authLoginRequestDto) {
        String email = authLoginRequestDto.getEmail();
        String password = authLoginRequestDto.getPassword();
        TokenInfo tokenInfo = authService.login(email, password).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return tokenInfo;
    }

    @PostMapping("/reissue-token")
    private TokenInfo reissueToken(@RequestHeader("refreshToken") String refreshToken, @RequestBody ReissueTokensRequestDto reissueTokensRequestDto) {
        TokenInfo tokenInfo = authService.reissueTokens(refreshToken, reissueTokensRequestDto);
        return tokenInfo;
    }

    @GetMapping("/user-info")
    @ResponseBody
    public String getUserinfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            System.out.println("로그인 된 상태");
            return userDetails.getUsername();
        }
        return "확인 불가";
    }

}
