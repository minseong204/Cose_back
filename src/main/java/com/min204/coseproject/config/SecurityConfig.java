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
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/auth/email/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/users/user/profile").permitAll()
                .antMatchers("/contents/**").permitAll()
                .antMatchers("/comments/**").permitAll()
                .antMatchers("/comments/**").permitAll()
                .antMatchers("**/hearts").hasRole("USER")
                .antMatchers("/email/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/auth/kakao/**").permitAll()
                .antMatchers("/oauth/callback/kakao").permitAll()
                .antMatchers("/v1/auth/kakao").permitAll()
                .antMatchers("/api/auth/google/**").permitAll()
                .antMatchers(("/api/auth/google")).permitAll()
                .antMatchers("/oauth/callback/google").permitAll()
                .antMatchers("/v1/auth/google").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/check-email").permitAll()
                .antMatchers(HttpMethod.POST, "/courses/**").permitAll()
                .antMatchers(HttpMethod.GET, "/follow/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/follow/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/follow/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/location/search").permitAll()  // 추가된 부분
                .antMatchers(HttpMethod.GET, "/location/keyword").permitAll()
                .antMatchers(HttpMethod.POST, "/scrap/content/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/scrap/course/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/scrap/contents").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/scrap/courses").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/scrap/**").hasRole("USER")
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
