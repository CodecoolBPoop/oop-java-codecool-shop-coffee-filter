package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.INTERNAL;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoSQL.getInstance();
        int userId = 1;

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

        Order latestOrder = orderDataStore.getLatestUnfinishedOrderByUser(userId);

        if (null != action || productId != 0) {
            Product product = productDataStore.find(productId);
            if (product != null) {
                int status = 201;
                if (latestOrder == null) {
                    if (action.equals("add")) {
                        orderDataStore.add(product, userId);
                        resp.setStatus(status);
                    } else {
                        resp.sendError(412, "Invalid data");
                    }
                } else {
                    if (action.equals("add")) {
                        orderDataStore.addNewItemToOrder(product, userId);
                    } else if (action.equals("remove")) {
                        orderDataStore.removeItemFromOrder(product, userId);
                        status = 200;
                    }
                }
                resp.setStatus(status);
                JSONObject cartAsJSON = orderDataStore.getLastShoppingCartByUser(userId);
                cartAsJSON.put("itemNumber", orderDataStore.getLatestUnfinishedOrderByUser(userId).getNumberOfItemsInCart());

                resp.setContentType("application/json;charset=UTF-8");

                ServletOutputStream out = resp.getOutputStream();

                out.print(cartAsJSON.toString());
            } else {
                resp.sendError(412, "Invalid data\nNo such product");
            }
        }
    }

}

