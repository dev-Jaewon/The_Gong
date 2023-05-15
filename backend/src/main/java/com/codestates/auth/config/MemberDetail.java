package com.codestates.auth.config;

import com.codestates.auth.oauth.utils.AuthorityUtils;
import com.codestates.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Map;
public class MemberDetail extends Member implements UserDetails, OAuth2User {

    private Member member;

    private Map<String, Object> attributes;

    MemberDetail(Member member){ //s가 영향이 있다.
        setMemberId(member.getMemberId());
        setNickname(member.getNickname());
        setImageUrl(member.getImageUrl());
        setEmail(member.getEmail());
        setPassword(member.getPassword());
        setStatus(member.getStatus());
    }

    public MemberDetail(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }


    public static MemberDetail of(Member member, Map<String, Object> attributes) {
        return new MemberDetail(member, attributes);
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.getAuthorities(member.getRoles());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    @Override
    public String getName() {
        return attributes.get("sub").toString();
    }
}
