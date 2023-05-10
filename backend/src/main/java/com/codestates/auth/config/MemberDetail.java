package com.codestates.auth.config;

import com.codestates.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class MemberDetail extends Member implements UserDetails {
    MemberDetail(Member member){
        setMemberId(member.getMemberId());
        setNickname(member.getNickname());
        setImageUrl(member.getImageUrl());
        setEmail(member.getEmail());
        setPassword(member.getPassword());
        setStatus(member.getStatus());
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
}
