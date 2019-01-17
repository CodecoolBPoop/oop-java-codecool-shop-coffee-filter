package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Status;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {

    private List<Order> orders = new ArrayList<>();
    private static OrderDaoMem instance = null;
    private int lastId = 1;

    private OrderDaoMem() {

    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(int userId, Order order) {
        order.setId(lastId++);
        orders.add(order);
    }

    @Override
    public Order find(int id) {
        return orders.get(id);
    }

    @Override
    public void remove(int id) {
        if (id < orders.size() && id >= 0) {
            orders.remove(id);
        }
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public void addNewItemToOrder(Product product, int orderId) {
        orders.get(orderId).addProductToCart(product);
    }

    @Override
    public void removeItemFromOrder(Product product, int orderId) {
        orders.get(orderId).removeProductFromCart(product);
    }

    @Override
    public Order getLatestUnfinishedOrderByUser(int userId) {
        return orders.stream()
                .filter(order -> order.getUserId() == userId)
                .filter(order -> !order.getStatus().equals(Status.PAID))
                .filter(order -> !order.getStatus().equals(Status.SHIPPED))
                .findFirst()
                .orElse(null);
    }
}
