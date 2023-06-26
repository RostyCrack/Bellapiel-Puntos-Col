//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.cancelation;

import java.util.List;

public class CancelationResponse {
    private String approbationNumber;
    private List<Balance> balances;
    private Long mainBalance;

    public String getApprobationNumber() {
        return this.approbationNumber;
    }

    public List<Balance> getBalances() {
        return this.balances;
    }

    public Long getMainBalance() {
        return this.mainBalance;
    }

    public void setApprobationNumber(final String approbationNumber) {
        this.approbationNumber = approbationNumber;
    }

    public void setBalances(final List<Balance> balances) {
        this.balances = balances;
    }

    public void setMainBalance(final Long mainBalance) {
        this.mainBalance = mainBalance;
    }

    public CancelationResponse(final String approbationNumber, final List<Balance> balances, final Long mainBalance) {
        this.approbationNumber = approbationNumber;
        this.balances = balances;
        this.mainBalance = mainBalance;
    }

    public CancelationResponse() {
    }
}
