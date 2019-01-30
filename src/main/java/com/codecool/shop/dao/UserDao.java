package com.codecool.shop.dao;

public interface UserDao {

    void add(String name, String pswd, String eMail);
    boolean checkUserLogin(String name, String pswd);
    void findByID(int id);
    void remove(int id);

}
