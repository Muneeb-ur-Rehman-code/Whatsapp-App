package com.example.myapplication.models;

public class User {
    private int id;
    private String mobileNumber;
    private String password;
    private long createdAt;

    public User() {
    }

    public User(String mobileNumber, String password) {
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}