package com.codestates.auth.token;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenizer jwtTokenizer;


    // key 생성
    public String getIngredients() {
        String secretKey = jwtTokenizer.getSecretKey();
        return jwtTokenizer.encodedBase64SecretKey(secretKey);
    }

    // member 추출
    public Member getMemberFromToken(Claims claims) {
        String memberId = String.valueOf(claims.get("memberId", Integer.class));
        return memberService.findMember(Long.parseLong(memberId));
    }



    //액세스+리프레시 토큰 반환
    public AuthDto convertToDto(Member member) {
        AuthDto authDto = new AuthDto();
        authDto.setMemberId(member.getMemberId());
        authDto.setNickname(member.getNickname());
        authDto.setImageUrl(member.getImageUrl());
        return authDto;
    }

    public Map<String, String> getGeneratedTokens(String refreshToken, String key) {
        Claims claims = jwtTokenizer.getClaimsRefresh(refreshToken);
        String accessToken = jwtTokenizer.generateAccessToken(claims,claims.getSubject(), claims.getIssuedAt(), key);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }



    //정보반환 + 액세스토큰 만료시 새토큰 반환
    private AuthDto convertToDtoWithToken(Member member, String token) {
        AuthDto authDto = new AuthDto();
        authDto.setMemberId(member.getMemberId());
        authDto.setNickname(member.getNickname());
        authDto.setImageUrl(member.getImageUrl());
        authDto.setAccessToken(token);
        return authDto;
    }

    public AuthDto regenerationToken(HttpServletRequest request, Claims claims, String token, String key) {
        String refreshToken = request.getHeader("RefreshToken");
        long findMemberId = jwtTokenizer.getMemberIdRefresh(refreshToken);
        claims = Jwts.claims().setSubject(String.valueOf(findMemberId));
        token = jwtTokenizer.regenerateAccessToken(claims, key);
        Member member = getMemberFromToken(claims);
        return convertToDtoWithToken(member,token);
    }

    public AuthDto getAuthMemberInfo(Claims claims) {
        Member member = getMemberFromToken(claims);
        return convertToDto(member);
    }
}
