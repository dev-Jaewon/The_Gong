package com.codestates.auth.token;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.member.entity.Member;
import com.codestates.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenizer jwtTokenizer;


    @PostMapping("/refresh")
    public ResponseEntity PostNewAccessToken(@RequestBody Map<String, String> refreshTokenMap) {
        String refreshToken = refreshTokenMap.get("refreshToken");
        String secretKey = jwtTokenizer.getSecretKey();
        String key = jwtTokenizer.encodedBase64SecretKey(secretKey);

        if(refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!jwtTokenizer.validateRefresh(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtTokenizer.getClaimsRefresh(refreshToken);
        String accessToken = jwtTokenizer.generateAccessToken(claims,claims.getSubject(), claims.getIssuedAt(), key);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok().body(tokens);
    }


    @GetMapping
    public ResponseEntity<AuthDto> getAuthMember(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String secretKey = jwtTokenizer.getSecretKey();
        String key = jwtTokenizer.encodedBase64SecretKey(secretKey);

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // 액세스토큰이 만료된 경우
            String refreshToken = request.getHeader("RefreshToken");
            long findMemberId = jwtTokenizer.getMemberIdRefresh(refreshToken);
            claims = Jwts.claims().setSubject(String.valueOf(findMemberId));
            token = jwtTokenizer.regenerateAccessToken(claims, key);
        }

        String memberId = String.valueOf(claims.get("sub", Integer.class));
        Member member = memberService.findMember(Long.parseLong(memberId));
        AuthDto authDto = convertToDto(member);
        return ResponseEntity.ok().body(authDto);
    }


    private AuthDto convertToDto(Member member) {
        AuthDto authDto = new AuthDto();
        authDto.setMemberId(member.getMemberId());
        authDto.setNickname(member.getNickname());
        authDto.setImageUrl(member.getImageUrl());
        return authDto;
    }
}
