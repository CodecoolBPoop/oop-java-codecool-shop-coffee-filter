package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.AddressHandler;
import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressHandlerSQL extends DataBaseConnect implements AddressHandler {

    private static AddressHandlerSQL instance = null;

    private AddressHandlerSQL() {
    }

    public static AddressHandlerSQL getInstance() {
        if (instance == null) {
            instance = new AddressHandlerSQL();
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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public Address getAddressById(int id) {
        String sql = "SELECT id, c.name AS country, state, postal_code, city, street, house_number, story, door, first_name, last_name " +
                "FROM delivery_addresses " +
                "LEFT JOIN countries c ON c.id = delivery_addresses.country " +
                "WHERE delivery_addresses.id = ?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);
            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    int addressId = resultSet.getInt("id");
                    String country = resultSet.getString("country");
                    String state = resultSet.getString("state");
                    String postalCode = resultSet.getString("postal_code");
                    String city = resultSet.getString("city");
                    String street = resultSet.getString("street");
                    int houseNumber = resultSet.getInt("house_number");
                    String story = resultSet.getString("story");
                    String door = resultSet.getString("door");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    return new Address(addressId, country, state, postalCode, city, street, houseNumber, story, door, firstName, lastName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
