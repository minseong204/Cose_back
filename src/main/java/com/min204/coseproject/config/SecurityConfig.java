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
                .antMatchers(
                        "/auth/**",
                        "/auth/email/**",
                        "/users/**",
                        "/users/user/profile",
                        "/contents/**",
                        "/comments/**",
                        "/email/**",
                        "/api/auth/**",
                        "/oauth/callback/**",
                        "/v1/auth/**",
                        "/auth/login",
                        "/auth/check-email",
                        "/courses/**",
                        "/location/search",
                        "/location/keyword"
                ).permitAll()
                .antMatchers("/**/hearts").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/follow/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/follow/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/follow/**").hasRole("USER")
                .antMatchers(
                        HttpMethod.POST,
                        "/scrap/content/**",
                        "/scrap/course/**"
                ).hasRole("USER")
                .antMatchers(
                        HttpMethod.GET,
                        "/scrap/contents",
                        "/scrap/courses"
                ).hasRole("USER")
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