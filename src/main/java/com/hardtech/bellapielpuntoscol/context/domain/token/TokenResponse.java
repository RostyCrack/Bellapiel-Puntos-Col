//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("scope")
    private String scope;
    private LocalDateTime expiresAt;
    private Boolean isTokenExpired = false;

    public TokenResponse() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public String getScope() {
        return this.scope;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiration() {
        this.expiresAt = LocalDateTime.now().plusSeconds(this.expiresIn);
        this.isTokenExpired = false;
    }

    public Boolean getIsTokenExpired() {
        return !this.expiresAt.isAfter(LocalDateTime.now());
    }
}
