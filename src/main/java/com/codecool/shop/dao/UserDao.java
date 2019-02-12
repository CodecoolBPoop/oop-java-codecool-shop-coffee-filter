package com.codecool.shop.dao;

import com.codecool.shop.model.User;

public interface UserDao {

    void add(String name, String pswd, String eMail);
    boolean checkIsExists(String name, String email);
    boolean checkNameAndPassword(String name, String password);
    int getUserIdByUsername(String name);
    User getUserById(int id);

}
