package com.min204.coseproject.config;

import com.min204.coseproject.auth.filter.JwtVerificationFilter;
import com.min204.coseproject.auth.handler.OAuth2UserSuccessHandler;
import com.min204.coseproject.auth.handler.UserAuthenticationFailureHandler;
import com.min204.coseproject.auth.jwt.JwtTokenizer;
import com.min204.coseproject.auth.utils.RedisUtil;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtVerificationFilter jwtVerificationFilter;
    private final JwtTokenizer jwtTokenizer;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    @Bean
    protected SecurityFilterChain configurer(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, "/", "/users/email-check", "/users/*/info").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/", "/users/login", "/users/logout").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/users/*", "/contents/**", "/comments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contents/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/contents/", "/comments/**", "**/hearts").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE).hasRole("USER")
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                        .successHandler(new OAuth2UserSuccessHandler(jwtTokenizer, userRepository, redisUtil))

                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((AuthenticationEntryPoint) new UserAuthenticationFailureHandler()))
                .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}