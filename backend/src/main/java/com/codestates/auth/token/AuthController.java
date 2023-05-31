package com.codestates.auth.token;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.auth.utils.ErrorResponse;
import com.codestates.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/refresh")
    public ResponseEntity PostNewAccessToken(@RequestBody AuthDto.Refresh requestBody) {

        String refreshToken = requestBody.getRefreshToken();
        if(!jwtTokenizer.validateRefresh(refreshToken)){
            return ResponseEntity.badRequest().build();
        }


        Member member = authService.findMember(requestBody.getMemberId());
        String accessToken = authService.delegateAccessToken(member);

        // new access token
        return ResponseEntity.ok(accessToken);
    }


//    @PostMapping("/refresh2")
//    public ResponseEntity PostNewAccessTokens2(@RequestBody AuthDto.Refresh requestBody) {
//
//        String refreshToken = requestBody.getRefreshToken();
//
//        if(!jwtTokenizer.validateRefresh(refreshToken)){
//            return ResponseEntity.badRequest().build();
//        }
//
//        long memberId = jwtTokenizer.getMemberIdRefresh(refreshToken);
//        Member member = memberService.findMember(memberId);
//        String accessToken = authenticationFilter.delegateAccessToken(member);
//
//        // new access token
//        return ResponseEntity.ok(accessToken);
//    }
//



    @GetMapping
    public ResponseEntity getAuthMember(HttpServletRequest request) {

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        log.info("token : " +token);

        String key = authService.getIngredients();
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            log.info("claims : " + claims);

        } catch (ExpiredJwtException e) {
            // 액세스토큰이 만료된 경우
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED,"액세스 토큰 만료");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        AuthDto authDto = authService.getAuthMemberInfo(claims);
        return ResponseEntity.ok().body(authDto);
    }
}
