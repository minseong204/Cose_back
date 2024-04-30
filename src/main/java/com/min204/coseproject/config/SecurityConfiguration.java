package com.min204.coseproject.config;

import com.min204.coseproject.auth.filter.JwtAuthenticationFilter;
import com.min204.coseproject.auth.filter.JwtVerificationFilter;
import com.min204.coseproject.auth.handler.UserAccessDeniedHandler;
import com.min204.coseproject.auth.handler.UserAuthenticationEntryPoint;
import com.min204.coseproject.auth.handler.*;
import com.min204.coseproject.auth.jwt.JwtTokenizer;
import com.min204.coseproject.auth.utils.CustomAuthorityUtils;
import com.min204.coseproject.auth.utils.RedisUtil;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .accessDeniedHandler(new UserAccessDeniedHandler())
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers(HttpMethod.GET, "/users/emailCheck/*").permitAll()
                        .antMatchers(HttpMethod.POST, "/users/", "/users/login").permitAll()
                        .antMatchers(HttpMethod.GET, "/users/*/info").permitAll()
                        .antMatchers(HttpMethod.PATCH, "/users/*").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/contents/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/contents/").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/contents/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/comments/**").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/comments/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "**/hearts").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE).hasRole("USER")
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2UserSuccessHandler(jwtTokenizer, userRepository, redisUtil))
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtVerificationFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        AuthenticationManager authenticationManager = authenticationManagerBean();
        return new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, redisUtil);
    }

    @Bean
    public JwtVerificationFilter jwtVerificationFilter() {
        return new JwtVerificationFilter(jwtTokenizer, authorityUtils, redisUtil);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        // Configure and return the AuthenticationManager instance
    }

}