package com.min204.coseproject.auth.jwt;

import com.min204.coseproject.auth.utils.CustomAuthorityUtils;
import com.min204.coseproject.auth.utils.RedisUtil;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Valid
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtils;

    @PostMapping("/reissue")
    public ResponseEntity reissueToken(@RequestHeader("Refresh") @NotBlank String refreshToken,
                                       HttpServletResponse response) {
        String encodeBase64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        try {
            jwtTokenizer.verifySignature(refreshToken, encodeBase64SecretKey);
        } catch (SignatureException | MalformedJwtException | DecodingException exception) {
            throw new BusinessLogicException(ExceptionCode.INVALID_VALUES);
        }

        Jws<Claims> claims = jwtTokenizer.getClaims(refreshToken, encodeBase64SecretKey);

        Map<String, Object> map = new HashMap<>();
        String email = claims.getBody().getSubject();

        // redis 에 refresh token 이 없을경우 예외 처리
        if (redisUtils.get(email) == null) {
            throw new BusinessLogicException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        List<String> roles = authorityUtils.createRoles(email);
        map.put("username", email);
        map.put("roles", roles);

        Date expriation = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String newAccessToken = jwtTokenizer.generateAccessToken(map, email, expriation, encodeBase64SecretKey);
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
