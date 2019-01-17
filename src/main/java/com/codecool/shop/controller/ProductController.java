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

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    boolean isCategorySet = false;
    boolean isSupplierSet = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
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
            float amountToPay = latestOrder.getAmountToPay();
            System.out.println(amountToPay);
            context.setVariable("cart", latestOrder.getShoppingCart());
            context.setVariable("amountToPay", amountToPay);
        }
        context.setVariable("recipient", "World");
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("products", productDataStore.getAll());
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

}
