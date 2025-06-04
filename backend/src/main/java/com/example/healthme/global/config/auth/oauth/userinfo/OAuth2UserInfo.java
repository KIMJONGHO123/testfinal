package com.example.healthme.global.config.auth.oauth.userinfo;

import java.util.Map;

public interface OAuth2UserInfo {
    String getName();
    String getEmail();
    String getProvider();
    String getProviderId();
    Map<String, Object> getAttributes();
}
