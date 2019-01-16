package com.codecool.shop.model;

public class LineItem {

    private Product product;
    private int quantity = 0;
    private float price;
    private float priceOfOne;

    public LineItem(Product product) {
        this.product = product;
        priceOfOne = product.getDefaultPrice();
        increaseQuantity();
    }

    public Product getProduct() {
        return product;
    }

    public void increaseQuantity() {
        quantity ++;
        price = priceOfOne * quantity;
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
}
