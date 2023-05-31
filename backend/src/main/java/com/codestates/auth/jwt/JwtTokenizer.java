package com.codestates.auth.jwt;

import com.codestates.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    @Value("${jwt.access.header}")
    @Getter
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    @Getter
    private String refreshHeader;

//    private Key key;
//    @PostConstruct
//    public void init() {
//        log.info("[init] JwtTokenProvider 내 secretKey : {} 초기화 시작 ", secretKey);
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        key = Keys.hmacShaKeyFor(keyBytes);
//        log.info("[init] JwtTokenProvider 내 secretKey : {} 초기화 완료", secretKey);
//    }

    public String encodedBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public Key getKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }



    //엑세스 토큰 생성
    public String generateAccessToken(Map<String, Object> claims, String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }
    public String generateAccessToken(String email, List<String> roles, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        log.info("[generateToken] accessToken 생성");

        Date date = getExpiration(accessTokenExpirationMinutes);
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);


        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(date)
                .signWith(key)
                .compact();
    }
    public String generateRefreshToken(String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        log.info("[generateToken] refreshToken 생성");
        Date date = getExpiration(accessTokenExpirationMinutes);
        return Jwts.builder()
                .setExpiration(date)
                .signWith(key)
                .compact();
    }


    //리프레쉬 토큰 생성
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    //jwtAuthenticationManager -> jwtTokenizer copy
    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("username", member.getEmail());


        String subject = String.valueOf(member.getMemberId());
        Date expiration = getExpiration(getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = encodedBase64SecretKey(getSecretKey());

        String accessToken = generateAccessToken(claims,subject,expiration,base64EncodedSecretKey);
        return accessToken;
    }


    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(jws);
        return claims;
    }


    public Date getExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();
        return expiration;
    }


    public boolean validateRefresh(String refreshToken) {
        String key = encodedBase64SecretKey(secretKey);

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            new IllegalArgumentException("잘못된 리프레시 토큰입니다",e);
        }
        return false;
    }

    //토큰에서 email분리
    public String getUserEmail(String token) {
        log.info("토큰 기반 회원 구별 정보 추출");
        String base64EncodedSecretKey = encodedBase64SecretKey(getSecretKey());
        Key key = getKey(base64EncodedSecretKey);

        String info = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.info("토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정");
    }


}
