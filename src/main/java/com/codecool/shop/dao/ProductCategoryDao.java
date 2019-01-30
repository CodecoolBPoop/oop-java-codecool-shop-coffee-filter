package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.sql.SQLException;
import java.util.List;

public interface ProductCategoryDao {

    void add(ProductCategory category) throws ClassNotFoundException, SQLException;
    ProductCategory find(int id) throws ClassNotFoundException, SQLException;
    void remove(int id) throws ClassNotFoundException, SQLException;

    List<ProductCategory> getAll() throws ClassNotFoundException, SQLException;

}
