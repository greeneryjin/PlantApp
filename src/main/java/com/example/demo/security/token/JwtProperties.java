package com.example.demo.security.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Period;
import java.util.Date;

@Getter
@Component
@Slf4j
public class JwtProperties {

    private final String secret;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofDays(1).toMillis(); //하루
    private static final long REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(14).toMillis(); // 14일

    public JwtProperties(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    //액세스 토큰 발급
    public String createAccessToken(String accessToken){
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("snsId", accessToken)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //리프레시 토큰 발급
    public String createRefreshToken(String refreshToken) {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .claim("snsId", refreshToken)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims getSnsId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (SignatureException e) {
            log.error("기존 인증이 다릅니다. {}", e);
            throw new JwtException("기존 인증이 다릅니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. {}", e);
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.error("JWT 올바르게 구성되지 않았습니다. {}", e);
            throw new JwtException("JWT 올바르게 구성되지 않았습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하는 JWT 토큰이 아닙니다. {}", e);
            throw new JwtException("지원하는 JWT 토큰이 아닙니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰을 넣어주세요 {}", e);
            throw new JwtException("유효하지 않는 토큰입니다.");
        }
    }

    //리프레시 토큰 만료 검사
    public boolean checkToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }  catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. {}", e);
            return false;
        }
    }
}
