//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.shared;

public class Points {
    private String pointsTypeCode;
    private String pointsTypeName;
    private double pointsAmount;

    public String getPointsTypeCode() {
        return this.pointsTypeCode;
    }

    public void setPointsTypeCode(String pointsTypeCode) {
        this.pointsTypeCode = pointsTypeCode;
    }

    public String getPointsTypeName() {
        return this.pointsTypeName;
    }

    public void setPointsTypeName(String pointsTypeName) {
        this.pointsTypeName = pointsTypeName;
    }

    public double getPointsAmount() {
        return this.pointsAmount;
    }

    public void setPointsAmount(double pointsAmount) {
        this.pointsAmount = pointsAmount;
    }

    public Points(final String pointsTypeCode, final String pointsTypeName, final double pointsAmount) {
        this.pointsTypeCode = pointsTypeCode;
        this.pointsTypeName = pointsTypeName;
        this.pointsAmount = pointsAmount;
    }
}
