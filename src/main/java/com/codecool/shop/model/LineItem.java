package com.codecool.shop.model;

public class LineItem {

    private Product product;
    private int quantity;
    private float price;

    public LineItem(Product product) {
        this.product = product;
        price = product.getDefaultPrice();
        quantity = 0;
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

    public String getProductName() {
        return product.getName();
    }

    @Override
    public String toString() {
        return getProductName() + ", " + getQuantity() + ", " + getPrice();
    }

    public void decreaseQuantity() {
        quantity = Math.max(quantity--, 0);
    }
}
