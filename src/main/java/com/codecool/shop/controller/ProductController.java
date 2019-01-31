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
import org.omg.CORBA.INTERNAL;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private boolean isCategorySet = false;
    private boolean isSupplierSet = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Session
        HttpSession session = req.getSession(false);

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoSQL.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        Map mainPageFilters = new HashMap<>();

        //Filters
        String category = req.getParameter("category");
        String supplier = req.getParameter("supplier");
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        int userId = 1;

        if (!(category == null || Integer.parseInt(category) == 0)) {
            isCategorySet = true;
            mainPageFilters.put("filteredCategory", category);
        } else {
            isCategorySet = false;
        }

        if (!(supplier == null || Integer.parseInt(supplier) == 0)) {
            isSupplierSet = true;
            mainPageFilters.put("filteredSupplier", supplier);
        } else {
            isSupplierSet = false;
        }

        setAllProductVisible(productDataStore);
        setProductVisibilityBasedOnCategoryFilter(productDataStore, category);
        setProductVisibilityBasedOnSupplierFilter(productDataStore, supplier);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("mainPageFilters", mainPageFilters);
//        context.setVariables(params);

        Order latestOrder = orderDataStore.getLatestUnfinishedOrderByUser(1);
        if (latestOrder != null) {
            Map<Integer, LineItem> cart = latestOrder.getShoppingCart();
            if (cart != null) {
                float amountToPay = latestOrder.getAmountToPay();
                context.setVariable("cart", cart);
                context.setVariable("amountToPay", amountToPay);
                context.setVariable("order", latestOrder);
            }
        }
        context.setVariable("recipient", "World");
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("products", productDataStore.getAll());
        if (session != null) {
            context.setVariable("username", session.getAttribute("username"));
        }
        engine.process("product/index.html", context, resp.getWriter());

//        Map params = new HashMap<>();
//        params.put("category", productCategoryDataStore.find(1));
//        params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
//        context.setVariables(params);
    }

    private void setAllProductVisible(ProductDao productDataStore) {
        for (int i = 0; i < productDataStore.getAll().size(); i++) {
            Product product = productDataStore.getAll().get(i);
            product.setVisibility(true);
        }
    }

    private void setProductVisibilityBasedOnCategoryFilter(ProductDao productDataStore, String category) {
        for (int i = 0; i < productDataStore.getAll().size(); i++) {
            Product product = productDataStore.getAll().get(i);
            if (isCategorySet) {
                if (product.getProductCategory().getId() != Integer.parseInt(category)) {
                    product.setVisibility(false);
                }
            }
        }
    }

    private void setProductVisibilityBasedOnSupplierFilter(ProductDao productDataStore, String supplier) {
        for (int i = 0; i < productDataStore.getAll().size(); i++) {
            Product product = productDataStore.getAll().get(i);
            if (isSupplierSet) {
                if (product.getSupplier().getId() != Integer.parseInt(supplier)) {
                    product.setVisibility(false);
                }
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int boughtItemId = Integer.parseInt(req.getParameter("boughtItem"));
        int userId = 1;
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order latestOrder = orderDataStore.getLatestUnfinishedOrderByUser(1);
        ProductDao productDataStore = ProductDaoMem.getInstance();

        Product product = productDataStore.find(boughtItemId);
        System.out.println("bought: " + product.getName());
        orderDataStore.addNewItemToOrder(product, latestOrder);

        resp.sendRedirect("/");
    }

}
