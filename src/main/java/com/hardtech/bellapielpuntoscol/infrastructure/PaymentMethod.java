//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

public class PaymentMethod {
    private String code;
    private double amount;

    public PaymentMethod() {
    }

    public String toString() {
        return "PaymentMethod{, code='" + this.code + "', amount=" + this.amount + "}";
    }

    public String getCode() {
        return this.code;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }
}
