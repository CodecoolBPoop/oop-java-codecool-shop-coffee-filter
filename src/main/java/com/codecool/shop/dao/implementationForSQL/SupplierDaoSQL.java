package com.codecool.shop.dao.implementationForSQL;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import static com.codecool.shop.dao.DataBaseConnect.executeQuery;
import java.sql.*;
import java.util.List;


public class SupplierDaoSQL implements SupplierDao {

    @Override
    public void add(Supplier supplier) {
        String query = "INSERT INTO suppliers (id, name, description) VALUES " +
                "('" + supplier.getId() + "', '" + supplier.getName() + "','" + supplier.getDescription() + "');";
        executeQuery(query);
    }

    @Override
    public Supplier find(int id) {
        String query = "SELECT * FROM suppliers WHERE id ='" + id + "';";
        executeQuery(query);

        //preparedstatement
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM suppliers WHERE id = '" + id + "';";
        executeQuery(query);
    }

    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM suppliers;";
        executeQuery(query);
    }

}
