package com.codestates.auth.token;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.auth.utils.ErrorResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenizer jwtTokenizer;


    @PostMapping("/refresh")
    public ResponseEntity PostNewAccessToken(@RequestBody Map<String, String> refreshTokenMap) {

        String refreshToken = refreshTokenMap.get("refreshToken");
        String key = authService.getIngredients();

        if(refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!jwtTokenizer.validateRefresh(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> tokens = authService.getGeneratedTokens(refreshToken, key);
        return ResponseEntity.ok().body(tokens);
    }




    @GetMapping
    public ResponseEntity getAuthMember(HttpServletRequest request) {

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String key = authService.getIngredients();
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // 액세스토큰이 만료된 경우
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN,"액세스 토큰 만료");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        AuthDto authDto = authService.getAuthMemberInfo(claims);
        return ResponseEntity.ok().body(authDto);
    }
}
