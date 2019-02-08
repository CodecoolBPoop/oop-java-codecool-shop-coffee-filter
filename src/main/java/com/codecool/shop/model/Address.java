package com.codecool.shop.model;

public class Address {
    private int id;
    private String country;
    private String state;
    private String postalCode;
    private String city;
    private String street;
    private String houseNumber;
    private String storey;
    private String door;
    private String firstName;
    private String lastName;


    public Address(int id, String country, String state, String postalCode, String city, String street, String houseNumber, String storey, String door, String firstName, String lastName) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.storey = storey;
        this.door = door;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() { return id; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode;}
    public String getCity() { return city; }
    public String getStreet() {return street; }
    public String getHouseNumber() {return houseNumber; }
    public String getStorey() {return storey; }
    public String getDoor() {return door; }
    public String getFirstName() {return firstName; }
    public String getLastName() {return lastName; }

}
