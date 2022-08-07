package com.example.antifraud.entity;

import com.example.antifraud.dto.Limit;
import com.example.antifraud.entity.enums.Region;
import com.example.antifraud.entity.enums.Type;
import com.example.antifraud.service.FraudIpService;
import com.example.antifraud.service.StolenCardService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private long amount;
    @Column
    private Type type;
    @Column
    private String number;
    @Column
    private String ip;
    @Column
    private Region region;
    @Column
    private Date date;
    @ElementCollection
    private List<String> info;
    private Type feedback;

    @JsonIgnore
    public Type getFeedback() {
        return feedback;
    }

    @JsonProperty("feedback")
    public String getFeedbackToStr() {
        return feedback!=null?feedback.name():"";
    }

    public void setFeedback(Type feedback) {
        this.feedback = feedback;
    }

    @JsonProperty("transactionId")
    public Long getId() {
        return id;
    }

    public Transaction(int amount) {
        this.amount = amount;
    }

    public Transaction() {
        info=new ArrayList<>();
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
        if(amount<=0) return;
        if(amount<= Limit.getInstance().getAllowedLimit()){
            type=Type.ALLOWED;
        }else if (amount<=Limit.getInstance().getManualLimit()){
            addInfo("amount");
            type=Type.MANUAL_PROCESSING;
        }else {
            addInfo("amount");
            type=Type.PROHIBITED;
        }
    }

    @JsonProperty("result")
    public Type getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonIgnore
    public String getInfo() {
        if(amount<=1500 && info.size()>1) info.remove("amount");
        return type==Type.ALLOWED?"none":info.toString().replaceAll("[\\[\\]]","");
    }

   public void addInfo(String info){
        this.info.add(info);
   }
    public void addInfo(String info,Type type){
        this.info.add(info);
        this.type=type;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonIgnore
    public void ipValidate(FraudIpService fraudIpService, List<Transaction> transactionList){
        List<FraudIP> fraudIPList = fraudIpService.findAll();
        if (fraudIPList.stream().anyMatch(fraudIP -> fraudIP.getIp().equals(this.getIp()))) {
            this.addInfo("ip", Type.PROHIBITED);
        }
        Set<String> ipSet = transactionList.stream().map(Transaction::getIp).collect(Collectors.toSet());
        ipSet.remove(this.getIp());
        if (ipSet.size() == 2) {
            this.addInfo("ip-correlation", Type.MANUAL_PROCESSING);
        } else if (ipSet.size() > 2) {
            this.addInfo("ip-correlation", Type.PROHIBITED);
        }
    }
    @JsonIgnore
    public void regionValidate(List<Transaction> transactionList){
        Set<Region> regionSet = transactionList.stream().map(Transaction::getRegion).collect(Collectors.toSet());
        regionSet.remove(this.getRegion());
        if (regionSet.size() == 2 ) {
            this.addInfo("region-correlation", Type.MANUAL_PROCESSING);
        } else if (regionSet.size() > 2) {
            this.addInfo("region-correlation", Type.PROHIBITED);
        }
    }
    @JsonIgnore
    public void stolenCardValidate(StolenCardService stolenCardService, List<Transaction> transactionList){
        List<StolenCard> stolenCards = stolenCardService.findAll();
        if (stolenCards.stream().anyMatch(stolenCard -> stolenCard.getNumber().equals(this.getNumber()))) {
            this.addInfo("card-number", Type.PROHIBITED);
        }
    }
}
