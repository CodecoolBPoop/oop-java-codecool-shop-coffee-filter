package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        ProductDao productDataStore = ProductDaoSQL.getInstance();
        OrderDao orderDataStore = OrderDaoSQL.getInstance();
        Integer userIdInSession = (Integer) session.getAttribute("userId");
        int userId = userIdInSession != null ? userIdInSession : 0;

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        String json = sb.toString();
        String action = null;
        int productId = 0;

        try {
            JSONObject jsonObject = new JSONObject(json);
            action = jsonObject.getString("action");
            productId = jsonObject.getInt("id");
        } catch (JSONException e) {
            resp.sendError(412, "Invalid data\nError parsing JSON request string");
        }

        if (null != action && productId != 0) {
            Product product = productDataStore.find(productId);
            if (product != null) {
                int status = 201;
                JSONObject cartAsJSON = new JSONObject();
                if (userId == 0) {
                    Order temporaryOrder = (Order) session.getAttribute("temporaryOrder");
                    if (action.equals("add")) {
                        temporaryOrder.addProductToCart(product);
                        System.out.println(temporaryOrder.toString());
                        resp.setStatus(status);

                    } else {
                        temporaryOrder.removeProductFromCart(product);
                        System.out.println(temporaryOrder.toString());
                        status = 200;
                    }
                    cartAsJSON = temporaryOrder.getCartAsJSON();
                } else if (userId > 0) {
                    if (action.equals("add")) {
                        orderDataStore.addNewItemToOrder(product, userId);
                        resp.setStatus(status);
                    } else {
                        orderDataStore.removeItemFromOrder(product, userId);
                        status = 200;
                    }
                    cartAsJSON = orderDataStore.getLastShoppingCartByUserAsJSON(userId);
                } else {
                    resp.sendError(412, "Invalid data\nNo such user");
                }
                resp.setStatus(status);
                resp.setContentType("application/json;charset=UTF-8");
                ServletOutputStream out = resp.getOutputStream();
                out.print(cartAsJSON.toString());
            } else {
                resp.sendError(412, "Invalid data\nNo such product");
            }
        }
    }
}

