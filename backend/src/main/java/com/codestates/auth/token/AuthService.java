package com.codestates.auth.token;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
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
        String memberId = String.valueOf(claims.get("memberId", Long.class));
        return memberService.findMember(Long.parseLong(memberId));
    }



    public AuthDto convertToDto(Member member) {
        AuthDto authDto = new AuthDto();
        authDto.setMemberId(member.getMemberId());
        authDto.setNickname(member.getNickname());
        authDto.setImageUrl(member.getImageUrl());
        return authDto;
    }


    public AuthDto getAuthMemberInfo(Claims claims) {
        Member member = getMemberFromToken(claims);
        return convertToDto(member);
    }
}
