package com.example.antifraud.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FraudIP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Column
    private String ip;

    public FraudIP() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = checkIP(ip)?ip:null;
    }
    public static boolean checkIP(String ip){
        String[] digits=ip.split("\\.");
        boolean flag=true;
        if(digits.length!=4) return false;
        for(String digit:digits){
            int digitOfNumber=Integer.parseInt(digit);
            if(digitOfNumber<0 || digitOfNumber>255) flag=false;
        }
        return flag;
    }
}
