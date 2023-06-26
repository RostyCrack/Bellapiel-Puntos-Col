//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.accumulation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hardtech.bellapielpuntoscol.context.domain.shared.Balance;
import com.hardtech.bellapielpuntoscol.context.domain.shared.MainBalance;
import java.util.List;

public class AccumulationResponse {
    @JsonProperty
    private String approbationNumber;
    @JsonProperty
    private List<Balance> balances;
    @JsonProperty
    private List<Object> issuedCoupons;
    @JsonProperty
    private List<Object> joinedLotteries;
    @JsonProperty
    private MainBalance mainBalance;

    public AccumulationResponse() {
    }

    public String getApprobationNumber() {
        return this.approbationNumber;
    }

    public List<Balance> getBalances() {
        return this.balances;
    }

    public List<Object> getIssuedCoupons() {
        return this.issuedCoupons;
    }

    public List<Object> getJoinedLotteries() {
        return this.joinedLotteries;
    }

    public MainBalance getMainBalance() {
        return this.mainBalance;
    }

    @JsonProperty
    public void setApprobationNumber(final String approbationNumber) {
        this.approbationNumber = approbationNumber;
    }

    @JsonProperty
    public void setBalances(final List<Balance> balances) {
        this.balances = balances;
    }

    @JsonProperty
    public void setIssuedCoupons(final List<Object> issuedCoupons) {
        this.issuedCoupons = issuedCoupons;
    }

    @JsonProperty
    public void setJoinedLotteries(final List<Object> joinedLotteries) {
        this.joinedLotteries = joinedLotteries;
    }

    @JsonProperty
    public void setMainBalance(final MainBalance mainBalance) {
        this.mainBalance = mainBalance;
    }
}
