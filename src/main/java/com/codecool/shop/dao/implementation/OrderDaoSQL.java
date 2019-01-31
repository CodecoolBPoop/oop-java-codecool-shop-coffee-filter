package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDaoSQL extends DataBaseConnect implements OrderDao {
    @Override
    public void add(int userId, Order order) {
        String query = "INSERT INTO orders (order_date, latest_update, status, user_id) VALUES (?, ?, 1, ?)";

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, order.getDate().toString());
            preparedStatement.setString(2, order.getDate().toString());
            preparedStatement.setInt(3, order.getUserId());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public void add(int userId, Product product) {

    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public void addNewItemToOrder(Product product, int orderId) {

    }

    @Override
    public void addNewItemToOrder(Product product, Order order) {

    }

    @Override
    public void removeItemFromOrder(Product product, int orderId) {

    }

    @Override
    public void removeItemFromOrder(Product product, Order order) {

    }

    @Override
    public Order getLatestUnfinishedOrderByUser(int userId) {
        return null;
    }

    @Override
    public JSONObject getLastShoppingCartByUser(int userId) {
        return null;
    }
}
