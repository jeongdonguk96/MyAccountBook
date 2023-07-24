package com.accountbook.myaccountbook.config;

import com.accountbook.myaccountbook.jwt.JwtAuthenticationFilter;
import com.accountbook.myaccountbook.jwt.JwtAuthorizationFilter;
import com.accountbook.myaccountbook.jwt.JwtProcess;
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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
//    private final JwtProcess jwtProcess;

    // 로그인 시큐리티 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .apply(new CustomSecurityFilterManager());
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .httpBasic();
//        http
//                .formLogin()
//                .loginPage("/member/login.html");
        http
                .authorizeHttpRequests()
                .antMatchers("/", "/join", "/login", "/logout", "/api/join", "/api/login", "/api/checkId", "api/refreshToken", "/error")
                .permitAll()
                .anyRequest()
                .authenticated();

        return http.build();
    }


    // JWT 필터 등록
    public class CustomSecurityFilterManager
            extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, new JwtProcess(refreshTokenRepository, memberRepository)));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager, new JwtProcess(refreshTokenRepository, memberRepository)));
            super.configure(builder);
        }
    }


    // 정적 리소스 시큐리티에서 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }


    // 패스워드 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
