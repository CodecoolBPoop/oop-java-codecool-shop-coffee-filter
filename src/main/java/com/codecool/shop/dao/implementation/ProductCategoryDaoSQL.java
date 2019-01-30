package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.sql.*;

public class ProductCategoryDaoSQL extends DataBaseConnect implements ProductCategoryDao {
    private static ProductCategoryDaoSQL instance = null;

    public static ProductCategoryDaoSQL getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoSQL();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        String sql = "INSERT INTO product_category (name, description, department) " + "VALUES (?, ?, ?)";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            String name = category.getName();
            String description = category.getDescription();
            String department = category.getDepartment();
            pstatement.setString(1, name);
            pstatement.setString(2, description);
            pstatement.setString(3, department);

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
    public ProductCategory find(int id) {
        String sql = "SELECT * FROM product_category WHERE id=?";

        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String department = resultSet.getString("department");

                    return new ProductCategory(name, description, department);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException("No such result");
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM product_category WHERE id=?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);

            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String department = resultSet.getString("department");
                }
            }
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
    public List<ProductCategory> getAll() {
        String sql = "SELECT * FROM product_category";
        List<ProductCategory> data = new ArrayList<>();

        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = pstatement.executeQuery()) {
                while(resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String department = resultSet.getString("department");

                    ProductCategory productCategory = new ProductCategory(name, description, department);
                    data.add(productCategory);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return data;
        }
    }
}

