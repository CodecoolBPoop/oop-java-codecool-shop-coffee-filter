package com.codecool.shop.model;

public class User {
    private int id;
    private String userName;
    private String email;

    public User(int id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public int getId() {return id; }
    public String getUserName() {return userName; }
    public String getEmail() { return email; }
}
