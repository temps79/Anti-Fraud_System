package com.example.auth.entity;

public enum Operation {
    LOCK("LOCK",false),
    UNLOCK("UNLOCK",true);
    private String name;
    private boolean value;

    Operation(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public static Operation asString(String str){
        for(Operation operation:Operation.values()){
            if(operation.name.equals(str)) return operation;
        }
        return Operation.LOCK;
    }

    public boolean isValue() {
        return value;
    }
}
