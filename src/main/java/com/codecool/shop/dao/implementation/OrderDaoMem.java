package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {

    private List<Order> orders = new ArrayList<>();
    private static OrderDaoMem instance = null;

    private OrderDaoMem() {

    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {

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
    public void addNewItemToOrder(Product product) {

    }

    @Override
    public void removeItemFromOrder(Product product) {

    }
}
