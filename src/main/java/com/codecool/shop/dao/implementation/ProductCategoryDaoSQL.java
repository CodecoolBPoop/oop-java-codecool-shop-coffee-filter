package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.util.List;
import java.lang.String;
import java.sql.*;

public class ProductCategoryDaoSQL extends DataBaseConnect implements ProductCategoryDao {

    @Override
    public void add(ProductCategory category) {
        try {
            Connection connection = super.getDbConnection();
            String sql = "INSERT INTO product_category (name, description, department) " + "VALUES (?, ?, ?)";
            pstatement = connection.prepareStatement(sql);
            String name = category.getName();
            String description = category.getDescription();
            String department = category.getDepartment();
            pstatement.setString(1, name);
            pstatement.setString(2, description);
            pstatement.setString(3, department);

            setResultSet(pstatement.executeQuery());

        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Failed to insert into table due to non-SQL error");
            e.printStackTrace();
        } finally {
            try {
                super.closeDataBaseConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ProductCategory find(int id) {
        try {
            Connection connection = super.getDbConnection();
            String sql = "SELECT * FROM product_category WHERE id=?";
            pstatement = connection.prepareStatement(sql);
            pstatement.setInt(1, id);

            setResultSet(pstatement.executeQuery());

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String department = resultSet.getString("department");

                return new ProductCategory(name, description, department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                super.closeDataBaseConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }
}
