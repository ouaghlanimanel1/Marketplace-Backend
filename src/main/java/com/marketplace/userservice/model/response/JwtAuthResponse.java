package com.marketplace.userservice.model.response;

import com.marketplace.userservice.model.User;
import com.marketplace.userservice.model.dto.UserDto;

public class JwtAuthResponse {
    private String accessToken;
    private UserDto user;
    private String tokenType = "Bearer";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
