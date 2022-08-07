package com.example.antifraud.dto;

public class Limit {
    private int allowedLimit;
    private int manualLimit;
    private static Limit INSTANCE;

    public static Limit getInstance(){
        if(INSTANCE==null){
            INSTANCE=new Limit();
        }
        return INSTANCE;
    }

    private Limit() {
        allowedLimit=200;
        manualLimit=1500;

    }
    public void increaseLimitAllowed(long transactionValue){
        this.allowedLimit= (int) Math.ceil(0.8 * this.allowedLimit + 0.2*transactionValue);
    }
    public void increaseLimitManual(long transactionValue){
        this.manualLimit= (int) Math.ceil(0.8 * this.manualLimit + 0.2*transactionValue);
    }
    public void reduceLimitAllowed(long transactionValue){
        this.allowedLimit= (int) Math.ceil(0.8 * this.allowedLimit - 0.2*transactionValue);
    }
    public void reduceLimitManual(long transactionValue){
        this.manualLimit= (int) Math.ceil(0.8 * this.manualLimit - 0.2*transactionValue);
    }
    public int getAllowedLimit() {
        return allowedLimit;
    }

    public int getManualLimit() {
        return manualLimit;
    }
}
