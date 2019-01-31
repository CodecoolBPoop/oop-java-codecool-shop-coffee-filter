package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

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

    private void add(int userId, Order order) {
        order.setId(++lastId);
        orders.add(order);
    }

    @Override
    public void add(Product product, int userId) {
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
    public void removeItemFromOrder(Product product, int userId) {
        Order order = getLatestUnfinishedOrderByUser(userId);
        order.removeProductFromCart(product);
        if (order.getNumberOfItemsInCart() == 0) {
            orders.remove(order);
        }
    }

    @Override
    public Order getLatestUnfinishedOrderByUser(int userId) {
        return orders.stream()
                .filter(order -> order.getUserId() == userId)
                .filter(order -> !order.getStatus().equals(Status.PAID))
                .filter(order -> !order.getStatus().equals(Status.SHIPPED))
                .filter(order -> !order.getStatus().equals(Status.CONFIRMED))
                .findFirst()
                .orElse(null);
    }

    @Override
    public JSONObject getLastShoppingCartByUser(int userId) {

        Order order = getLatestUnfinishedOrderByUser(userId);
        Map<Integer, LineItem> cart = order.getShoppingCart();
        List<JSONObject> cartItems = cart.entrySet().stream().map(x -> x.getValue().toJSON()).collect(Collectors.toList());
        JSONArray cartItemsAsJSON = new JSONArray(cartItems);
        JSONObject cartAsJSON = new JSONObject();
        cartAsJSON.put("items", cartItemsAsJSON);
        cartAsJSON.put("amount", order.getAmountToPay());
        return cartAsJSON;
    }
}
