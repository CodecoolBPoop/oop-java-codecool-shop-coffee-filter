package com.codecool.shop.model;

import java.util.HashMap;

public class Order {

    private int id;
    private int userId;
    private int date; //todo search for a valid date format
    private Status status;
    private HashMap<Integer, LineItem> shoppingCart = new HashMap<Integer, LineItem>();

    public Order(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public void addProductToCart(Product product) {
        Integer productId = product.getId();
        LineItem lineItem = null;
        if (shoppingCart.containsKey(productId)) {
            lineItem = shoppingCart.get(productId);
            lineItem.increaseQuantity();
        } else {
            lineItem = new LineItem(product);
        }
        shoppingCart.put(productId, lineItem);
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public HashMap<Integer, LineItem> getShoppingCart() {
        return shoppingCart;
    }

    public int getDate() {
        return date;
    }
}
