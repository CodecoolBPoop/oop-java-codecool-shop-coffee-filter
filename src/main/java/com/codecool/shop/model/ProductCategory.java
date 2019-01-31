package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class ProductCategory extends BaseModel {
    private String department;
    private List<Product> products;

    public ProductCategory(int id, String name, String description, String department) {
        super(id, name, description);
        setDepartment(department);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
//        this.products.add(product);
        System.out.println("PRODUCT CATEGORY == ADD PRODUCT CALLED");
    }

    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final ProductCategory s = (ProductCategory) o;
        if (this.getId() != s.getId()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}