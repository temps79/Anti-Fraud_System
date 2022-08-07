package com.example.auth.entity;

public enum Role {
    MERCHANT("ROLE_MERCHANT","MERCHANT"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR","ADMINISTRATOR"),
    SUPPORT("ROLE_SUPPORT","SUPPORT");
    private final String value;
    private final String name;

    Role(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
    public static Role asString(String name){
        for(Role role:Role.values()){
            if(role.name.equals(name)) return role;
        }
        return Role.MERCHANT;
    }
}
