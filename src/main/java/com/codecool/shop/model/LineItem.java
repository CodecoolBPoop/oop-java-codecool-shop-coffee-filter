package com.codecool.shop.model;

public class LineItem {

    private Product product;
    private int quantity;
    private float price;
    private String name;

    public LineItem(Product product) {
        this.product = product;
        price = product.getDefaultPrice();
        quantity = 1;
        name = product.getName();
    }

    public Product getProduct() {
        return product;
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
        quantity = Math.max(quantity--, 0);
    }
}
