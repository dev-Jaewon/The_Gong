package com.codestates.auth.oauth;

import com.codestates.auth.oauth.utils.AuthorityUtils;
import com.codestates.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;

    private List<String> role;


    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String email,
                            List<String> role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }

    public static CustomOAuth2User of(Member member,
                                      Map<String, Object> attributes,
                                      OAuthAttributes oAuthAttributes) {
        return new CustomOAuth2User(
                AuthorityUtils.getAuthorities(member.getRoles()),
                attributes,
                oAuthAttributes.getProviderId(),
                member.getEmail(),
                member.getRoles()
        );
    }
}
