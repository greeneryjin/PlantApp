package com.example.demo.security.config;

import com.example.demo.security.filter.JwtAuthorizationFilter;
import com.example.demo.security.handler.AccessDeniedHandlerIm;
import com.example.demo.security.repository.RefreshTokenRepository;
import com.example.demo.security.service.JwtAccountService;
import com.example.demo.security.token.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //jwt 설정
    @Autowired JwtProperties jwtProperties;

    //cors 설정
    @Autowired CorsConfig corsConfig;

    //jwt 인증 실패
    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    //jwt 인가 실패
    @Autowired AccessDeniedHandlerIm accessDeniedHandlerIm;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //react, spring boot 교차출처 허용
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
                .and()
                .cors().configurationSource(corsConfig.corsFilter())
                .and()
                .csrf()//csrf 안함
                .disable()
                .sessionManagement()//세션 사용 안함
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //회원가입, 로그인, GET은 허용
                .and()
                .authorizeRequests()
                .antMatchers(  "/v2/api-docs", "/configuration/ui", "/swagger-resources/**","/configuration/security", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                .antMatchers("/oauth2/**").permitAll() //oauth2 login 허용
                .antMatchers("/api/plantDic/**").permitAll()
                .anyRequest() //인증 시작
                .authenticated()
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/api/register");
        http
                //jwt 검증 확인
                .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                //인증 실패
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerIm)
                .authenticationEntryPoint(authEntryPoint);
        return http.build();
    }
}
