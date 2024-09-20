package com.accountbook.myaccountbook.config;

import com.accountbook.myaccountbook.jwt.JwtAuthenticationFilter;
import com.accountbook.myaccountbook.jwt.JwtAuthorizationFilter;
import com.accountbook.myaccountbook.jwt.JwtService;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final AccessDeniedHandler customAccessDeniedHandler;

    // 시큐리티를 설정한다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .apply(new CustomSecurityFilterManager());
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .httpBasic();
        http
                .formLogin()
                .loginPage("/login");
        http
                .authorizeHttpRequests()
                .antMatchers("/", "/join", "/login", "/logout", "/api/join", "/api/login", "/api/checkId", "api/refreshToken", "/error", "/actuator/**", "purchase", "cancel")
                .permitAll()
                .antMatchers("/book2")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated();
        http
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }


    // 커스텀해 구현한 JWT 인증/인가 필터를 등록한다.
    public class CustomSecurityFilterManager
            extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, new JwtService(refreshTokenRepository, memberRepository)));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager, new JwtService(refreshTokenRepository, memberRepository)));
            super.configure(builder);
        }
    }


    // 정적 리소스를 시큐리티에서 제외한다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }


    // 패스워드를 암호화한다.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
