package com.example.antifraud.dto;

import com.example.antifraud.entity.enums.Type;

public class FeedBack {
    private long transactionId;
    private Type feedback;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Type getFeedback() {
        return feedback;
    }

    public void setFeedback(Type feedback) {
        this.feedback = feedback;
    }
}
