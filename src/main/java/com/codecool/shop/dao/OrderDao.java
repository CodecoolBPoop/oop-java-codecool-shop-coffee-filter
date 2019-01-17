package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.util.List;

public interface OrderDao {

    void add(int userId, Order order);

    Order find(int id);

    void remove(int id);

    List<Order> getAll();

    void addNewItemToOrder(Product product, int orderId);

    void addNewItemToOrder(Product product, Order order);

    void removeItemFromOrder(Product product, int orderId);

    void removeItemFromOrder(Product product, Order order);

    Order getLatestUnfinishedOrderByUser(int userId);
}
