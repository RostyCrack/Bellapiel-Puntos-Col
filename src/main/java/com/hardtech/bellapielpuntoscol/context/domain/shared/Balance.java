//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Balance {
    @JsonProperty
    private Long bonusPointsEarned;
    @JsonProperty
    private Long pointsAmount;
    @JsonProperty
    private Long pointsBurned;
    @JsonProperty
    private Long pointsEarned;
    @JsonProperty
    private String pointsTypeCode;
    @JsonProperty
    private String pointsTypeName;

    public Balance() {
    }

    public Long getBonusPointsEarned() {
        return this.bonusPointsEarned;
    }

    public Long getPointsAmount() {
        return this.pointsAmount;
    }

    public Long getPointsBurned() {
        return this.pointsBurned;
    }

    public Long getPointsEarned() {
        return this.pointsEarned;
    }

    public String getPointsTypeCode() {
        return this.pointsTypeCode;
    }

    public String getPointsTypeName() {
        return this.pointsTypeName;
    }
}
