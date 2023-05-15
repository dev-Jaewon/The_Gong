package com.codestates.auth.oauth.service;


import com.codestates.auth.oauth.CustomOAuth2User;
import com.codestates.auth.oauth.OAuthAttributes;
import com.codestates.auth.oauth.userinfo.ProviderType;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 -> OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration()
                .getRegistrationId();
        ProviderType providerType = getProviderType(provider);
        String providerId = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("provider : {} ", provider);
        log.info("providerId : {}", providerId);
        log.info("attributes : {}", attributes);

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(providerType, providerId, attributes);

        log.info("username : {}", oAuthAttributes.getOAuth2UserInfo().getName());
        log.info("email : {}", oAuthAttributes.getOAuth2UserInfo().getEmail());

        Member member = getMember(oAuthAttributes, providerType);

        log.info("CustomOAuth2UserSerivce.loadUser() 종료");
        return CustomOAuth2User.of(member, attributes, oAuthAttributes);
    }

    private ProviderType getProviderType(String provider) {
        if (provider.equals("google")) {
            return ProviderType.GOOGLE;
        }

        if (provider.equals("naver")) {
            return ProviderType.NAVER;
        }

        throw new BusinessLogicException(ExceptionCode.PROVIDER_NOT_FOUND);
    }

    private Member getMember(OAuthAttributes attributes, ProviderType providerType) {
        return memberRepository.findByEmail(attributes.getOAuth2UserInfo().getEmail())
                .orElseGet(() -> createUser(attributes, providerType));
    }

    private Member createUser(OAuthAttributes attributes, ProviderType providerType) {
        Member member = attributes.toEntity(providerType,
                attributes.getOAuth2UserInfo(),
                passwordEncoder);
        return memberRepository.save(member);
    }
}

