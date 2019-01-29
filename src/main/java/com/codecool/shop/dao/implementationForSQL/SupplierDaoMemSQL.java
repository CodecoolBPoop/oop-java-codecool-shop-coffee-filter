package com.codecool.shop.dao.implementationForSQL;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import java.sql.*;

import java.util.List;

public class SupplierDaoMemSQL implements SupplierDao {

    @Override
    public void add(Supplier supplier) {
        String query = "INSERT INTO suppliers (id, name, description) VALUES " +
                "('\" + todo.title + \"', '\" + todo.id + \"', '\" + todo.status + \"');";
        executeQuery(query);
    }

    @Override
    public Supplier find(int id) {
        String query = "SELECT * FROM suppliers WHERE id ='" + id + "';";
        executeQuery(query);
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM suppliers WHERE id = '" + id +"';";
        executeQuery(query);
    }

    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM suppliers;";
        executeQuery(query);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
