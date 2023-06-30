//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class MainBalance {
    @JsonProperty("pointsValueInMoney")
    private long pointsValueInMoney;
    @JsonProperty("expiringPoints")
    private long expiringPoints;
    @JsonProperty("pointsAmount")
    private long pointsAmount;
    @JsonProperty("pointsMoneyRatio")
    private long pointsMoneyRatio;
    @JsonProperty("expirationDate")
    private LocalDate expirationDate;
    @JsonProperty("pointsEarned")
    private int pointsEarned;
    @JsonProperty("pointsBurned")
    private int pointsBurned;
    @JsonProperty("bonusPointsEarned")
    private int bonusPointsEarned;
    @JsonProperty("pointsBalance")
    private int pointsBalance;

    public MainBalance() {
    }

    public String toString() {
        long var10000 = this.pointsValueInMoney;
        return "MainBalance{pointsValueInMoney=" + var10000 + ", expiringPoints=" + this.expiringPoints + ", pointsAmount=" + this.pointsAmount + ", pointsMoneyRatio=" + this.pointsMoneyRatio + ", expirationDate=" + this.expirationDate + "}";
    }

    public long getPointsValueInMoney() {
        return this.pointsValueInMoney;
    }

    public long getExpiringPoints() {
        return this.expiringPoints;
    }

    public long getPointsAmount() {
        return this.pointsAmount;
    }

    public long getPointsMoneyRatio() {
        return this.pointsMoneyRatio;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public int getPointsEarned() {
        return this.pointsEarned;
    }

    public int getPointsBurned() {
        return this.pointsBurned;
    }

    public int getBonusPointsEarned() {
        return this.bonusPointsEarned;
    }

    public int getPointsBalance() {
        return this.pointsBalance;
    }

    @JsonProperty("pointsValueInMoney")
    public void setPointsValueInMoney(final long pointsValueInMoney) {
        this.pointsValueInMoney = pointsValueInMoney;
    }

    @JsonProperty("expiringPoints")
    public void setExpiringPoints(final long expiringPoints) {
        this.expiringPoints = expiringPoints;
    }

    @JsonProperty("pointsAmount")
    public void setPointsAmount(final long pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    @JsonProperty("pointsMoneyRatio")
    public void setPointsMoneyRatio(final long pointsMoneyRatio) {
        this.pointsMoneyRatio = pointsMoneyRatio;
    }

    @JsonProperty("expirationDate")
    public void setExpirationDate(final LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty("pointsEarned")
    public void setPointsEarned(final int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    @JsonProperty("pointsBurned")
    public void setPointsBurned(final int pointsBurned) {
        this.pointsBurned = pointsBurned;
    }

    @JsonProperty("bonusPointsEarned")
    public void setBonusPointsEarned(final int bonusPointsEarned) {
        this.bonusPointsEarned = bonusPointsEarned;
    }

    @JsonProperty("pointsBalance")
    public void setPointsBalance(final int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }
}
