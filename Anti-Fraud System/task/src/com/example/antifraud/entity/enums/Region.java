package com.example.antifraud.entity.enums;

public enum Region {
    EAP("EAP","East Asia and Pacific"),
    ECA("ECA","Europe and Central Asia"),
    HIC("HIC","High-Income countries"),
    LAC("LAC","Latin America and the Caribbean"),
    MENA("MENA","The Middle East and North Africa"),
    SA("SA","South Asia"),
    SSA("SSA","Sub-Saharan Africa");

    private String value;
    private String name;

    Region(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Region asString(String str){
        for(Region region:Region.values()){
            if(region.value.equals(str)) return region;
        }
        return Region.EAP;
    }
}
