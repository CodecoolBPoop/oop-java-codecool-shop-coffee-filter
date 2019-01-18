package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Order;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import org.omg.CORBA.INTERNAL;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        int userId = 1;

        System.out.println(req.getParameterMap().toString());
        String id = req.getParameter("id");
        int productId = Integer.getInteger(id);
        String action = req.getParameter("action");

        Order latestOrder = orderDataStore.getLatestUnfinishedOrderByUser(userId);
        Product product = productDataStore.find(productId);
        int status = 201;
        if (latestOrder == null) {
            if (action.equals("add")) {
                orderDataStore.add(userId, product);
                System.out.println(orderDataStore.getLatestUnfinishedOrderByUser(userId).getShoppingCart().toString());
                resp.setStatus(status);
            } else {
                resp.sendError(412, "Invalid data");
            }
        } else {
            if (action.equals("add")) {
                latestOrder.addProductToCart(product);
            } else if (action.equals("remove")) {
                latestOrder.removeProductFromCart(product);
                status = 200;
            }
            System.out.println(latestOrder.getShoppingCart().toString());
            resp.setStatus(status);
        }
    }

}

