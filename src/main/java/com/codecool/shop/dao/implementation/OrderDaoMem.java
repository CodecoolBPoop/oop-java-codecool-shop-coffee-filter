package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Status;

import java.util.ArrayList;
import java.util.Iterator;
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
    public void add(int userId, Product product) {
        Order order = new Order(userId, product);
        add(userId, order);
    }

    @Override
    public Order find(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public void remove(int id) {
        if (id > 0) {
            Iterator<Order> orderIterator = orders.listIterator();
            while (orderIterator.hasNext()) {
                if (orderIterator.next().getId() == id) {
                    orderIterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public void addNewItemToOrder(Product product, int orderId) {
        find(orderId).addProductToCart(product);
    }

    @Override
    public void addNewItemToOrder(Product product, Order order) {
        addNewItemToOrder(product, order.getId());
    }

    @Override
    public void removeItemFromOrder(Product product, int orderId) {
        Order order = find(orderId);
        order.removeProductFromCart(product);
        if (order.getNumberOfItemsInCart() == 0) {
            orders.remove(orderId);
        }
    }

    @Override
    public void removeItemFromOrder(Product product, Order order) {
        removeItemFromOrder(product, order.getId());
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
