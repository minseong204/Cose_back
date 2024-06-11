package com.min204.coseproject.auth.controller;

import com.min204.coseproject.auth.dto.req.AuthSigUpRequestDto;
import com.min204.coseproject.auth.service.AuthService;
import com.min204.coseproject.auth.dto.oAuthLoginParams.GoogleLoginParams;
import com.min204.coseproject.auth.dto.oAuthLoginParams.KakaoLoginParams;
import com.min204.coseproject.auth.dto.oAuthLoginParams.NaverLoginParams;
import com.min204.coseproject.auth.service.GoogleOAuthService;
import com.min204.coseproject.auth.service.OAuthService;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenInfo> signUpKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthService.handleOAuth(params));
    }

    @PostMapping("/naver")
    public ResponseEntity<TokenInfo> signUpNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthService.handleOAuth(params));
    }

    @PostMapping("/google")
    public ResponseEntity<com.min204.coseproject.jwt.TokenInfo> signUpGoogle(@RequestBody GoogleLoginParams params) {
        return ResponseEntity.ok(googleOAuthService.handleOAuth(params));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResBodyModel> signup(@RequestBody AuthSigUpRequestDto request) {
        authService.save(request);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, "회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<ResBodyModel> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        TokenInfo tokenInfo = authService.login(email, password)
                .orElseThrow(() -> new RuntimeException("로그인 실패"));
        return CoseResponse.toResponse(SuccessCode.SUCCESS, tokenInfo);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResBodyModel> reissueTokens(@RequestBody ReissueTokensRequestDto requestDto) {
        log.info("재발행 요청: {}, 리프레시 토큰으로: {}", requestDto.getEmail(), requestDto.getRefreshToken());
        TokenInfo tokenInfo = authService.reissueTokens(requestDto.getRefreshToken(), requestDto);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, tokenInfo);
    }

    /*
     * 중복 이메일 검사
     */
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
            Map<String, Object> data = new HashMap<>();
            data.put("email", e.getEmail());
            data.put("isAvailable", false);
            data.put("type", e.getLoginType() != null ? e.getLoginType().name() : null);
            response.put("data", data);
            return ResponseEntity.badRequest().body(response);
        }
    }
}