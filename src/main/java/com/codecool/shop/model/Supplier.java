package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Supplier extends BaseModel {
    private List<Product> products;

    public Supplier(int id, String name, String description) {
        super(id, name, description);
        this.products = new ArrayList<>();
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        System.out.println("Supplier ADD PRODUCT CALLED");
//        this.products.add(product);
    }

    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Supplier s = (Supplier) o;
        if (this.getId() != s.getId()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getSupplierName() {
        return this.name;
    }
}