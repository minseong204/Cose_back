package com.min204.coseproject.auth.controller;

import com.min204.coseproject.auth.dto.oAuthLoginParams.GoogleLoginParams;
import com.min204.coseproject.auth.dto.oAuthLoginParams.KakaoLoginParams;
import com.min204.coseproject.auth.dto.oAuthLoginParams.NaverLoginParams;
import com.min204.coseproject.auth.dto.req.AuthSigUpRequestDto;
import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.auth.service.AuthService;
import com.min204.coseproject.auth.service.GoogleOAuthService;
import com.min204.coseproject.auth.service.OAuthService;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;
    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/signup")
    public ResponseEntity<ResBodyModel> signup(@RequestBody AuthSigUpRequestDto request) {
        authService.save(request);
        return CoseResponse.toResponse(SuccessCode.SIGN_UP_SUCCESS, "회원가입 성공", HttpStatus.CREATED.value());
    }

    @PostMapping("/login")
    public ResponseEntity<ResBodyModel> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        TokenInfo tokenInfo = authService.login(email, password)
                .orElseThrow(() -> new RuntimeException("로그인 실패"));
        return CoseResponse.toResponse(SuccessCode.LOGIN_SUCCESS, tokenInfo, HttpStatus.OK.value());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResBodyModel> reissueTokens(@RequestBody ReissueTokensRequestDto requestDto) {
        TokenInfo tokenInfo = authService.reissueTokens(requestDto.getRefreshToken(), requestDto);
        return CoseResponse.toResponse(SuccessCode.REISSUE_SUCCESS, tokenInfo, HttpStatus.OK.value());
    }

    @PostMapping("/kakao")
    public ResponseEntity<ResBodyModel> signUpKakao(@RequestBody KakaoLoginParams params) {
        TokenInfo tokenInfo = oAuthService.handleOAuth(params);
        return CoseResponse.toResponse(SuccessCode.LOGIN_SUCCESS, tokenInfo, HttpStatus.ACCEPTED.value());
    }

    @PostMapping("/naver")
    public ResponseEntity<ResBodyModel> signUpNaver(@RequestBody NaverLoginParams params) {
        TokenInfo tokenInfo = oAuthService.handleOAuth(params);
        return CoseResponse.toResponse(SuccessCode.LOGIN_SUCCESS, tokenInfo, HttpStatus.ACCEPTED.value());
    }

    @PostMapping("/google")
    public ResponseEntity<ResBodyModel> signUpGoogle(@RequestBody GoogleLoginParams params) {
        TokenInfo tokenInfo = googleOAuthService.handleOAuth(params);
        return CoseResponse.toResponse(SuccessCode.LOGIN_SUCCESS, tokenInfo, HttpStatus.ACCEPTED.value());
    }

    @PostMapping("/check-email")
    public ResponseEntity<ResBodyModel> checkEmailExists(@RequestParam String email) {
        try {
            authService.existsByEmail(email);
            return CoseResponse.toResponse(SuccessCode.EMAIL_AVAILABLE, Map.of("email", email, "isAvailable", true));
        } catch (EmailAlreadyExistsException e) {
            return CoseResponse.toErrorResponse(ErrorCode.EMAIL_EXISTS, Map.of("email", email, "isAvailable", false));
        }
    }
}