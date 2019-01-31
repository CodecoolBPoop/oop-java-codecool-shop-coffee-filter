package com.codecool.shop.dao;

public interface AddressSavingDao {

    void add(String country, String state, String postalCode, String city, String street, String houseNumber, String story, String door, String firstName, String lastName);

}
