package com.codecool.shop.model;

public class LineItem {

    private Product product;
    private int quantity;
    private float price;
    private float priceOfOne;

    public LineItem(Product product) {
        this.product = product;
        priceOfOne = product.getDefaultPrice();
        setQuantity(1);
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.price = priceOfOne * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }
}
