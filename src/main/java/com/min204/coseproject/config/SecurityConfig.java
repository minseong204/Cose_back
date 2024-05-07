package com.min204.coseproject.config;

import com.min204.coseproject.jwt.JwtAuthenticationFilter;
import com.min204.coseproject.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/email-check/*").permitAll()//이메일 중복 체크
                .antMatchers(HttpMethod.POST, "/users/", "/users/login").permitAll() // 회원 가입
                .antMatchers(HttpMethod.POST, "/users/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/users/*/info").permitAll() // 회원 상세 정보 조회
                .antMatchers(HttpMethod.PATCH, "/users/*").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole("USER", "ADMIN") // 회원 조회
                .antMatchers(HttpMethod.GET, "/contents/**").permitAll()//게시글 조회
                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER") // 회원 삭제
                .antMatchers(HttpMethod.POST, "/contents/").hasRole("USER") // 컨텐츠 등록
                .antMatchers(HttpMethod.PATCH, "/contents/**").hasRole("USER") // 컨텐츠 편집
                .antMatchers(HttpMethod.POST, "/comments/**").hasRole("USER") // 후기 생성
                .antMatchers(HttpMethod.PATCH, "/comments/**").hasRole("USER") // 후기 수정
                .antMatchers(HttpMethod.POST, "**/hearts").hasRole("USER") // 좋아요
                .antMatchers(HttpMethod.DELETE).hasRole("USER") // 질문, 답변 삭제
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
