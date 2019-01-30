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
        String name = product.getName();
        String description = product.getDescription();
        float price = product.getFloatPrice();
        int stock = 1;
        String currencyString = product.getCurrency();
        String supplierString = product.getSupplier().getSupplierName();
        String productCategoryString = product.getProductCategory().getName();

        String sql =
                "WITH curid as (SELECT id FROM currencies WHERE currencies.currency = '" + currencyString + "' RETURNING id) "
                        + "WITH supid as (SELECT id FROM suppliers WHERE suppliers.name = '" + supplierString + "' RETURNING id) "
                        + "WITH prid as (SELECT id FROM product_category WHERE product_category.name = '" + productCategoryString + "' RETURNING id) "
                        + "INSERT INTO products (name, description, price, stock, currency, supplier_id, product_category_id)"
                        + "VALUES (?, ?, ?, ?, (SELECT id FROM curid), (SELECT id FROM supid), (SELECT id FROM prid))";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setString(1, name);
            pstatement.setString(2, description);
            pstatement.setFloat(3, price);
            pstatement.setInt(4, stock);

            pstatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
