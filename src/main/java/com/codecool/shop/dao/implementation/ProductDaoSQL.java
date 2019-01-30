package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;

import java.sql.*;
import java.util.List;

public class ProductDaoSQL extends DataBaseConnect implements ProductDao {
    private static ProductDaoSQL instance = null;

    public static ProductDaoSQL getInstance() {
        if (instance == null) {
            instance = new ProductDaoSQL();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, " +
                "currency, supplier_id, product_category_id)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            String name = product.getName();
            String description = product.getDescription();


        }

    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }
}
