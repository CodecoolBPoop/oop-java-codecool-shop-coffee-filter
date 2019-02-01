package com.codecool.shop.dao;

import com.codecool.shop.model.Address;

public interface AddressHandler {


    void add(String country, String state, String postalCode, String city, String street, String houseNumber, String story, String door, String firstName, String lastName);
    Address getAddressById(int id);

}
