package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDaoSQL extends DataBaseConnect implements OrderDao {

    private static OrderDaoSQL instance = null;

    private OrderDaoSQL() {

    }

    public static OrderDaoSQL getInstance() {
        if (instance == null) {
            instance = new OrderDaoSQL();
        }
        return instance;
    }

    @Override
    public void add(Product product, int userId) {

        String query = "WITH o AS (INSERT INTO orders (user_id) VALUES (?) RETURNING *)" +
                "INSERT INTO order_products (order_id, product_id, price) VALUES ((SELECT id FROM o), ?, ?)";

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setFloat(3, product.getDefaultPrice());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public Order find(int id) {
        String query = "SELECT * FROM orders WHERE id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = new Order(id);
                    order.setId(resultSet.getInt("id"));
                    addCartItems(order);
                    return order;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM order_products WHERE order_id = ?; " +
                "DELETE FROM orders WHERE id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public void addNewItemToOrder(Product product, int userId) {
        System.out.println(userId);
        Order order = getLatestUnfinishedOrderByUser(userId);
        int productId = product.getId();
        if (order != null) {
            if (order.getShoppingCart().containsKey(productId)) {
                increaseQuantityOfItemInOrder(order, productId);
            } else {
                addNewTypeOfItemToOrder(product, order, productId);
            }

        } else {
            add(product, userId);
        }
    }

    private void increaseQuantityOfItemInOrder(Order order, int productId) {
        String query = "UPDATE order_products SET quantity = quantity + 1 WHERE order_id = ? AND product_id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    private void addNewTypeOfItemToOrder(Product product, Order order, int productId) {

        String query = "INSERT INTO order_products (order_id, product_id, price) VALUES (?, ?, ?)";

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, productId);
            preparedStatement.setFloat(3, product.getDefaultPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public void removeItemFromOrder(Product product, int userId) {
        Order order = getLatestUnfinishedOrderByUser(userId);
        int orderId = order.getId();
        int productId = product.getId();
        HashMap<Integer, LineItem> shoppingCart = order.getShoppingCart();
        int typesInCart = shoppingCart.size();
        if (shoppingCart.get(productId).getQuantity() > 1) {
            decreaseQuantityOfItemInOrder(order, productId);
        } else {
            removeItemTypeFromOrder(orderId, productId);
            if (typesInCart == 1) {
                remove(orderId);
            }
        }

    }

    private void decreaseQuantityOfItemInOrder(Order order, int productId) {
        String query = "UPDATE order_products SET quantity = quantity - 1 WHERE order_id = ? AND product_id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    private void removeItemTypeFromOrder(int orderId, int productId) {
        String query = "DELETE FROM order_products WHERE product_id = ? AND order_id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public Order getLatestUnfinishedOrderByUser(int userId) {
        String query = "SELECT * FROM orders WHERE user_id = ? AND status IN (1, 2)";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = new Order(userId);
                    order.setId(resultSet.getInt("id"));
                    LocalDateTime orderDate = resultSet.getTimestamp("order_date").toLocalDateTime();
                    order.setOrderDate(orderDate);
                    LocalDateTime latestUpdate = resultSet.getTimestamp("latest_update").toLocalDateTime();
                    addCartItems(order);
                    order.setLatestUpdate(latestUpdate);
                    int status = resultSet.getInt("status");
                    order.setStatus(Status.getStatusByIntValue(status));
                    int deliveryAddressId = resultSet.getInt("delivery_address");
                    order.setDeliveryAddressId(deliveryAddressId);
                    return order;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
        return null;
    }

    private void addCartItems(Order order) {
        String query = "SELECT order_products.*, products.name FROM order_products JOIN products ON id = product_id WHERE order_id = ?";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, order.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    order.addToCArt(resultSet.getInt("product_id"),
                            resultSet.getInt("quantity"),
                            resultSet.getFloat("price"),
                            resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getLastShoppingCartByUser(int userId) {
        Order order = getLatestUnfinishedOrderByUser(userId);
        JSONObject cartAsJSON = new JSONObject();
        if (order != null) {
            Map<Integer, LineItem> cart = order.getShoppingCart();
            List<JSONObject> cartItems = cart.entrySet().stream().map(x -> x.getValue().toJSON()).collect(Collectors.toList());
            JSONArray cartItemsAsJSON = new JSONArray(cartItems);
            cartAsJSON.put("items", cartItemsAsJSON);
            cartAsJSON.put("amount", order.getAmountToPay());
            cartAsJSON.put("itemNumber", order.getNumberOfItemsInCart());
        } else {
            cartAsJSON.put("items", "");
        }
        return cartAsJSON;
    }

    @Override
    public int getLatestUnfinishedOrderIdByUser(int userId) {
        String query = "SELECT id FROM orders WHERE user_id = ? AND status IN (1, 2)";
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        }
        return 0;
    }
}
