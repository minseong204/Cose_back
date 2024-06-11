package com.min204.coseproject.config;

import com.min204.coseproject.jwt.JwtAuthenticationFilter;
import com.min204.coseproject.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                // 누구나 접근 가능
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/public/**").permitAll()
                // 특정 역할(ADMIN)을 가진 사용자만 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")
                // follow 관련 경로는 인증된 사용자만 접근 가능
                .antMatchers(HttpMethod.GET, "/follow/**").authenticated()
                .antMatchers(HttpMethod.POST, "/follow/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/follow/**").authenticated()
                // content 관련 경로는 인증된 사용자만 접근 가능
                .antMatchers(HttpMethod.GET, "/contents/**").authenticated()
                .antMatchers(HttpMethod.POST, "/contents/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/contents/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/contents/**").authenticated()
                // course 관련 경로는 인증된 사용자만 접근 가능
                .antMatchers(HttpMethod.GET, "/courses/**").authenticated()
                .antMatchers(HttpMethod.POST, "/courses/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/courses/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/courses/**").authenticated()
                // user 관련 경로는 인증된 사용자만 접근 가능
                .antMatchers(HttpMethod.GET, "/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/users/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/**").authenticated()
                // scrap 관련 경로는 인증된 사용자만 접근 가능
                .antMatchers(HttpMethod.GET, "/scraps/**").authenticated()
                .antMatchers(HttpMethod.POST, "/scraps/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/scraps/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/scraps/**").authenticated()
                // 나머지 모든 요청은 인증된 사용자만 접근 가능
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}