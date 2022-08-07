package com.example.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @Column
    private String password;
    @Column
    private String role;

    @Column
    private Operation operation;

    public User() {
    }

    public User(long id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("role")
    public String getRole() {
        return Role.asString(role).getName();
    }

    @JsonIgnore
    public String getRoleStr(){
        return role;
    }

    @JsonIgnore
    public boolean roleIsEmpty(){
        return role==null;
    }

    @JsonProperty("operation")
    public void setOperationAsString(String operation) {
        this.operation = Operation.asString(operation);
    }

    @JsonIgnore
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
