//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.cancelation;

public class Balance {
    private Long pointsAmount;
    private String pointsTypeCode;
    private String pointsTypeName;

    public Long getPointsAmount() {
        return this.pointsAmount;
    }

    public String getPointsTypeCode() {
        return this.pointsTypeCode;
    }

    public String getPointsTypeName() {
        return this.pointsTypeName;
    }

    public void setPointsAmount(final Long pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    public void setPointsTypeCode(final String pointsTypeCode) {
        this.pointsTypeCode = pointsTypeCode;
    }

    public void setPointsTypeName(final String pointsTypeName) {
        this.pointsTypeName = pointsTypeName;
    }

    public Balance(final Long pointsAmount, final String pointsTypeCode, final String pointsTypeName) {
        this.pointsAmount = pointsAmount;
        this.pointsTypeCode = pointsTypeCode;
        this.pointsTypeName = pointsTypeName;
    }
}
