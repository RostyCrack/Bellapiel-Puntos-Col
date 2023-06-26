//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hardtech.bellapielpuntoscol.context.domain.shared.MainBalance;

public class AccountResponse {
    @JsonProperty("mobilePhoneNo")
    private String mobilePhoneNo;
    @JsonProperty("mainBalance")
    private MainBalance mainBalance;
    @JsonProperty("allowRedemption")
    private boolean allowRedemption;
    @JsonProperty("totpActive")
    private boolean totpActive;
    @JsonProperty("name")
    private String name;
    @JsonProperty("allowAccrual")
    private boolean allowAccrual;
    @JsonProperty("email")
    private String email;

    public AccountResponse() {
    }

    public String getMobilePhoneNo() {
        return this.mobilePhoneNo;
    }

    public void setMobilePhoneNo(String value) {
        this.mobilePhoneNo = value;
    }

    public MainBalance getMainBalance() {
        return this.mainBalance;
    }

    public void setMainBalance(MainBalance value) {
        this.mainBalance = value;
    }

    public boolean getAllowRedemption() {
        return this.allowRedemption;
    }

    public void setAllowRedemption(boolean value) {
        this.allowRedemption = value;
    }

    public boolean getTotpActive() {
        return this.totpActive;
    }

    public void setTotpActive(boolean value) {
        this.totpActive = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public boolean getAllowAccrual() {
        return this.allowAccrual;
    }

    public void setAllowAccrual(boolean value) {
        this.allowAccrual = value;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String toString() {
        String var10000 = this.mobilePhoneNo;
        return "AccountResponse{mobilePhoneNo='" + var10000 + "', mainBalance=" + String.valueOf(this.mainBalance) + ", allowRedemption=" + this.allowRedemption + ", totpActive=" + this.totpActive + ", name='" + this.name + "', allowAccrual=" + this.allowAccrual + ", email='" + this.email + "'}";
    }
}
