package com.min204.coseproject.auth.controller;

import com.min204.coseproject.auth.dto.AuthLoginRequestDto;
import com.min204.coseproject.auth.dto.AuthSigUpRequestDto;
import com.min204.coseproject.auth.service.AuthService;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
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

    /*
     * 중복 이메일 검사
     * */
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestParam String email) {
        try {
            authService.existsByEmail(email);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용 가능한 이메일입니다.");
            response.put("data", Map.of(
                    "email", email,
                    "isAvailable", true
            ));
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "이미 사용 중인 이메일입니다.");
            response.put("data", Map.of(
                    "email", e.getEmail(),
                    "isAvailable", false,
                    "Type", e.getType()
            ));
            return ResponseEntity.badRequest().body(response);
        }
    }
}