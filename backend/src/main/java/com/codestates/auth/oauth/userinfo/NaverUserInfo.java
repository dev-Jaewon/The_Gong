package com.codestates.auth.oauth.userinfo;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProverId() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("id");
    }
    @Override
    public String getEmail() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("name");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("profile");
    }

    private Map<String, Object> getStringObjectMap() {
        return (Map<String, Object>) attributes.get("response");
    }
}
