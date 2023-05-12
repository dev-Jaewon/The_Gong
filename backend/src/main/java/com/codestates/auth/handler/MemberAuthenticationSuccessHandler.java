package com.codestates.auth.handler;

import com.codestates.auth.config.MemberDetail;
import com.codestates.auth.config.MemberDetailService;
import com.codestates.auth.login.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login Successful 로그인 인증성공");

        MemberDetailService.MemberDetail memberDetail = (MemberDetailService.MemberDetail) authentication.getPrincipal();
        Long memberId = memberDetail.getMemberId();
        String nickname = memberDetail.getNickname();
        String imageUrl = memberDetail.getImageUrl(); //이미지 포함여부
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");

        if(accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setMemberId(memberId);
        responseDto.setNickname(nickname);
        responseDto.setImageUrl(imageUrl);
        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshToken);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }
}
