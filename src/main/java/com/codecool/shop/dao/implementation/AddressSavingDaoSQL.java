package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.AddressHandler;
import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressSavingDaoSQL extends DataBaseConnect implements AddressHandler {

    private static AddressSavingDaoSQL instance = null;

    private AddressSavingDaoSQL() {
    }

    public static AddressSavingDaoSQL getInstance() {
        if (instance == null) {
            instance = new AddressSavingDaoSQL();
        }
        return instance;
    }


    @Override
    public void add(String country, String state, String postalCode, String city, String street, String houseNumber, String story, String door, String firstName, String lastName) {
        String query = "INSERT INTO public.delivery_addresses (country, state, postal_code, city, street, house_number, story, door, first_name, last_name) VALUES ((SELECT id FROM public.countries WHERE name = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, state);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, street);
            preparedStatement.setString(6, houseNumber);
            preparedStatement.setString(7, story);
            preparedStatement.setString(8, door);
            preparedStatement.setString(9, firstName);
            preparedStatement.setString(10, lastName);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public Address getAddressById(int id) {
        return null;
    }
}
