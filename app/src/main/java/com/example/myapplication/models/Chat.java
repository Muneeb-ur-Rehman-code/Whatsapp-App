package com.example.myapplication.models;

public class Chat {
    private int id;
    private int user1Id;
    private int user2Id;
    private String lastMessage;
    private long lastMessageTime;
    private long createdAt;

    // For display purposes
    private String otherUserMobile;

    public Chat() {
    }

    public Chat(int user1Id, int user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createdAt = System.currentTimeMillis();
        this.lastMessage = "";
        this.lastMessageTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getOtherUserMobile() {
        return otherUserMobile;
    }

    public void setOtherUserMobile(String otherUserMobile) {
        this.otherUserMobile = otherUserMobile;
    }
}