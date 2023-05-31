package com.codestates.auth.oauth.handler;

import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.auth.oauth.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenizer jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("Oauth 로그인 성공");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        loginSuccess(response, oAuth2User);
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String base64EncodedSecretKey = jwtTokenProvider.encodedBase64SecretKey(jwtTokenProvider.getSecretKey());
        String accessToken = jwtTokenProvider.generateAccessToken(oAuth2User.getEmail(), oAuth2User.getRole(), base64EncodedSecretKey);
        String refreshToken = jwtTokenProvider.generateRefreshToken(base64EncodedSecretKey);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        response.sendRedirect(createURI(accessToken, refreshToken));
    }
    private String createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("thegong.site")
//                .port(3000)
                .path("/oauth")
                .queryParams(queryParams)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }

}
