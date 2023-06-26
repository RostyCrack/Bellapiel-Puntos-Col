//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.cancelation;

import com.hardtech.bellapielpuntoscol.infrastructure.TransactionIdentifier;

public class CancelationRequestBody {
    private String partnerCode;
    private TransactionIdentifier transactionIdentifier;
    private TransactionIdentifier originalTransactionIdentifier;

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public TransactionIdentifier getTransactionIdentifier() {
        return this.transactionIdentifier;
    }

    public TransactionIdentifier getOriginalTransactionIdentifier() {
        return this.originalTransactionIdentifier;
    }

    public void setPartnerCode(final String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public void setTransactionIdentifier(final TransactionIdentifier transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    public void setOriginalTransactionIdentifier(final TransactionIdentifier originalTransactionIdentifier) {
        this.originalTransactionIdentifier = originalTransactionIdentifier;
    }

    public CancelationRequestBody() {
    }
}
