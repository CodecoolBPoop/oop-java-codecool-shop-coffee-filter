package com.codecool.shop.dao;

public interface UserDao {

    void add(String name, String pswd, String eMail);
    boolean checkIsExists(String name, String email);
    boolean checkNameAndPassword(String name, String password);

}
