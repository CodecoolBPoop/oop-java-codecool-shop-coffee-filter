package com.codecool.shop.dao;

import com.codecool.shop.model.Address;

public interface AddressHandler {

    void add(String country, String state, String postalCode, String city, String street, String houseNumber, String storey, String door, String firstName, String lastName, int orderId);

    Address getAddressById(int id);

}
