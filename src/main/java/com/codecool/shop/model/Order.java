package com.codecool.shop.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Order {

    private int id;
    private int userId;
    private LocalDateTime date;
    private Timestamp orderDate;
    private Timestamp latestUpdate;
    private String statusSQL;
    private int deliveryAddressId;
    private Status status;
    private HashMap<Integer, LineItem> shoppingCart = new HashMap<>();

    public Order(int userId, Product product) {
        this.userId = userId;
        status = Status.NEW;
        addProductToCart(product);
    }
    /**Constructor for SQL**/
    public Order(int id, Timestamp orderDate, Timestamp latestUpdate, String status, int userId, int deliveryAddress) {
        this.id = id;
        this.orderDate = orderDate;
        this.latestUpdate = latestUpdate;
        this.statusSQL = status;
        this.userId = userId;
        this.deliveryAddressId = deliveryAddress;
    }

    public Order(int userId) {
        this.userId = userId;
        status = Status.NEW;
    }

    public void addProductToCart(Product product) {
        Integer productId = product.getId();
        LineItem lineItem;
        if (shoppingCart.containsKey(productId)) {
            lineItem = shoppingCart.get(productId);
            lineItem.increaseQuantity();
        } else {
            lineItem = new LineItem(product);
        }
        shoppingCart.put(productId, lineItem);
        date = LocalDateTime.now();
    }

    public int getNumberOfItemsInCart() {
        return shoppingCart.entrySet()
                .stream()
                .mapToInt(item -> item.getValue().getQuantity())
                .sum();
    }

    public void removeProductFromCart(Product product) {
        Integer productId = product.getId();
        LineItem lineItem;
        if (shoppingCart.containsKey(productId)) {
            lineItem = shoppingCart.get(productId);
            lineItem.decreaseQuantity();
            if (lineItem.getQuantity() == 0) {
                shoppingCart.remove(productId);
            } else {
                shoppingCart.put(productId, lineItem);
            }
        }
        date = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmountToPay() {
        List<Float> sums = shoppingCart.entrySet().stream().map(x -> x.getValue().getQuantity() * x.getValue().getPrice()).collect(Collectors.toList());
        float amount = 0;
        for (Float sum : sums
             ) {
            amount += sum;
        }
        return amount;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setStatus(Status status) {
        this.status = status;
        date = LocalDateTime.now();
    }

    public void addToCArt(int productId, int quantity, float price, String name) {
        LineItem item = new LineItem(quantity, price, name, productId);
        shoppingCart.put(productId, item);
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public Timestamp getLatestUpdate() {
        return latestUpdate;
    }

    public String getStatusSQL() {
        return statusSQL;
    }

    public int getDeliveryAddressId() {
        return deliveryAddressId;
    }
}
