package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private int userId;
    private int date; //todo search for a valid date format
    private Status status;
    private List<LineItem> shoppingCart= new ArrayList<>();
}
