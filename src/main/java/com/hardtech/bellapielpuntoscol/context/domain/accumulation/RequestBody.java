//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.context.domain.accumulation;

import com.hardtech.bellapielpuntoscol.infrastructure.PaymentMethod;
import com.hardtech.bellapielpuntoscol.infrastructure.TransactionIdentifier;
import java.util.ArrayList;
import java.util.List;

public class RequestBody {
    private String documentNo;
    private String documentType;
    private String partnerCode;
    private TransactionIdentifier transactionIdentifier;
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    public void append(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
    }

    public void setDocumentNo(final String documentNo) {
        this.documentNo = documentNo;
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
    }

    public void setPartnerCode(final String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public void setTransactionIdentifier(final TransactionIdentifier transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    public void setPaymentMethods(final List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getDocumentNo() {
        return this.documentNo;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public TransactionIdentifier getTransactionIdentifier() {
        return this.transactionIdentifier;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }

    public RequestBody() {
    }
}
