package com.codestates.auth.config;

import com.codestates.auth.handler.MemberAccessDeniedHandler;
import com.codestates.auth.handler.MemberAuthenticationEntryPoint;
import com.codestates.auth.handler.MemberAuthenticationFailureHandler;
import com.codestates.auth.handler.MemberAuthenticationSuccessHandler;
import com.codestates.auth.jwt.JwtAuthenticationFilter;
import com.codestates.auth.jwt.JwtTokenizer;
import com.codestates.auth.jwt.JwtVerificationFilter;
import com.codestates.auth.oauth.handler.OAuth2LoginFailureHandler;
import com.codestates.auth.oauth.handler.OAuth2LoginSuccessHandler;
import com.codestates.auth.oauth.service.CustomOAuth2UserService;
import com.codestates.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())

                .and()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())

                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserService);

        return http.build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        cors.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }



    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {

            AuthenticationManager manager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter authentication = new JwtAuthenticationFilter(manager, jwtTokenizer,memberRepository); //jwtAuthenticationFilter attemptAuthentication() 메서드에서 로그인 처리
            authentication.setFilterProcessesUrl("/members/login");
            authentication.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            authentication.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter verification = new JwtVerificationFilter(jwtTokenizer);
            builder .addFilter(authentication)
                    .addFilterAfter(verification, JwtAuthenticationFilter.class);
        }
    }
}

