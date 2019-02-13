package com.codecool.shop.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Order {

    private int id;
    private int userId;
    private LocalDateTime orderDate;
    private LocalDateTime latestUpdate;
    private int deliveryAddressId;
    private Status status;
    private HashMap<Integer, LineItem> shoppingCart = new HashMap<>();

    public Order(int userId, Product product) {
        this.userId = userId;
        status = Status.NEW;
        addProductToCart(product);
    }

    public Order(int userId) {
        this.userId = userId;
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

    public void addToCArt(int productId, int quantity, float price, String name) {
        LineItem item = new LineItem(quantity, price, name, productId);
        shoppingCart.put(productId, item);
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setLatestUpdate(LocalDateTime latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(int deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public JSONObject getCartAsJSON() {
        JSONObject cartAsJSON = new JSONObject();
        if (shoppingCart != null) {
            Map<Integer, LineItem> cart = getShoppingCart();
            List<JSONObject> cartItems = cart.entrySet().stream().map(x -> x.getValue().toJSON()).collect(Collectors.toList());
            JSONArray cartItemsAsJSON = new JSONArray(cartItems);
            cartAsJSON.put("items", cartItemsAsJSON);
            cartAsJSON.put("amount", getAmountToPay());
            cartAsJSON.put("itemNumber", getNumberOfItemsInCart());
        } else {
            cartAsJSON.put("items", "");
        }
        return cartAsJSON;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", status=").append(status);
        sb.append(", shoppingCart=").append(shoppingCart);
        sb.append('}');
        return sb.toString();
    }
}
