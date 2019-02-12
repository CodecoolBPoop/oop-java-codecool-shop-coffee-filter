package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.JSONObject;

import java.util.List;


public interface OrderDao {

    void add(Product product, int userId);

    Order find(int id);

    void remove(int id);

    List<Order> getAll();

    void addNewItemToOrder(Product product, int userId);

    void removeItemFromOrder(Product product, int userId);

    Order getLatestUnfinishedOrderByUser(int userId);

    JSONObject getLastShoppingCartByUser(int userId);

    int getLatestUnfinishedOrderIdByUser(int userId);
}
