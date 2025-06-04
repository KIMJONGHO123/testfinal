package com.example.healthme.global.config.security;

import com.example.healthme.domain.user.repository.UserRepository;
import com.example.healthme.global.config.auth.handler.loginHandler.CustomLoginFailureHandler;
import com.example.healthme.global.config.auth.handler.loginHandler.CustomLoginSuccessHandler;
import com.example.healthme.global.config.auth.handler.logoutHandler.CustomLogoutHandler;
import com.example.healthme.global.config.auth.handler.logoutHandler.CustomLogoutSuccessHandler;
import com.example.healthme.global.config.auth.jwt.JwtAuthorizationFilter;
import com.example.healthme.global.config.auth.jwt.JwtTokenProvider;
import com.example.healthme.global.config.auth.principal.PrincipalDetailsOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLoginFailureHandler customLoginFailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsOAuth2Service principalDetailsOAuth2Service;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/healthme/users/join",
                                "/healthme/users/check",
                                "/healthme/users/login",
                                "/healthme/result/**"
                        ).permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/healthme/users/login") // login URL 명시
                        .usernameParameter("userid") // 프론트 formData에 맞춤
                        .successHandler(customLoginSuccessHandler)
                        .failureHandler(customLoginFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/healthme/users/logout")
                        .permitAll()
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user
                                .userService(principalDetailsOAuth2Service)
                        )
                        .successHandler(customLoginSuccessHandler)
                );

        http.addFilterBefore(
                new JwtAuthorizationFilter(userRepository, jwtTokenProvider),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
