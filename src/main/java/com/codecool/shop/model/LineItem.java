package com.codecool.shop.model;

import org.json.JSONObject;

public class LineItem {

    private Product product;
    int productId;
    private int quantity;
    private float price;
    private String name;

    public LineItem(Product product) {
        this.product = product;
        price = product.getDefaultPrice();
        quantity = 1;
        name = product.getName();
        productId = product.id;
    }

    public LineItem(int quantity, float price, String name, int productId) {
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public float getTotalPrice() {
        return price * quantity;
    }

    public void increaseQuantity() {
        quantity ++;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + ", " + getQuantity() + ", " + getPrice();
    }

    public void decreaseQuantity() {
        quantity = Math.max(--quantity, 0);
    }

    public JSONObject toJSON() {
        JSONObject itemAsJSON = new JSONObject();
        itemAsJSON.put("id", productId);
        itemAsJSON.put("quantity", quantity);
        itemAsJSON.put("price", price);
        itemAsJSON.put("name", name);
        return itemAsJSON;
    }
}
