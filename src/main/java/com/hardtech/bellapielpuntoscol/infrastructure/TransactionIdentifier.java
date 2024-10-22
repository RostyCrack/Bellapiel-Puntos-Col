//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;

@Getter
public class TransactionIdentifier {
    private String terminalId;
    private String transactionId;
    private String cashierId;
    private String locationCode;
    private String transactionDate;
    private String nut;

    public String toString() {
        return "TransactionIdentifier{transactionId=" + this.transactionId + ", terminal_id='" + this.terminalId + "', transactionDate=" + this.transactionDate + ", locationCode='" + this.locationCode + "', nut='" + this.nut + "'}";
    }

    public void setTerminalId(final String terminalId) {
        this.terminalId = terminalId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public void setCashierId(final String cashierId) {
        this.cashierId = cashierId;
    }

    public void setLocationCode(final String locationCode) {
        this.locationCode = locationCode;
    }

    public void setTransactionDate(final String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setNut(final String nut) {
        this.nut = nut;
    }

    public TransactionIdentifier() {
    }
}
