package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.*;
import com.codecool.shop.dao.implementation.SupplierDaoSQL;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Session
        HttpSession session = req.getSession(false);

        ProductDao productDataStore = ProductDaoSQL.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoSQL.getInstance();
        SupplierDao supplierDataStore = SupplierDaoSQL.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        int userId = 1;

        //Filters
        Map mainPageFilters = new HashMap<>();
        String category = req.getParameter("category");
        String supplier = req.getParameter("supplier");
        List<ProductCategory> categories = new ArrayList<>();
        List<Supplier> suppliers = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        //Drop Down Filter settings
        if (!(category == null || Integer.parseInt(category) == 0)) {
            mainPageFilters.put("filteredCategory", category);
            categories.add(productCategoryDataStore.find(Integer.parseInt(category)));
        } else {
            categories.clear();
            categories = productCategoryDataStore.getAll();
        }

        if (!(supplier == null || Integer.parseInt(supplier) == 0)) {
            mainPageFilters.put("filteredSupplier", supplier);
            suppliers.add(supplierDataStore.find(Integer.parseInt(supplier)));
        } else {
            suppliers.clear();
            suppliers = supplierDataStore.getAll();
        }

        //Filter for Products
        if (!(category == null || Integer.parseInt(category) == 0)) {
            if (!(supplier == null || Integer.parseInt(supplier) == 0)) {
                products = productDataStore.getBy(Integer.parseInt(supplier), Integer.parseInt(category));
                categories.clear();
                suppliers.clear();
                categories.add(productCategoryDataStore.find(Integer.parseInt(category)));
                suppliers.add(supplierDataStore.find(Integer.parseInt(supplier)));
            } else {
                suppliers.clear();
                products = productDataStore.getByProductCategory(Integer.parseInt(category));
                for (Product product: products) {
                    int supplierID = product.getSupplier().getId();
                    Supplier s = supplierDataStore.find(supplierID);
                    if (suppliers.contains(s)) {
                        continue;
                    } else {
                        suppliers.add(s);
                    }
                }
            }
        } else if (!(supplier == null || Integer.parseInt(supplier) == 0)) {
            categories.clear();
            products = productDataStore.getBySupplier(Integer.parseInt(supplier));
            for (Product product: products) {
                int productCategoryId = product.getProductCategory().getId();
                ProductCategory pc = productCategoryDataStore.find(productCategoryId);
                if (categories.contains(pc)) {
                    continue;
                }
                categories.add(pc);
            }

        } else {
            suppliers = supplierDataStore.getAll();
            categories = productCategoryDataStore.getAll();
            products = productDataStore.getAll();
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("mainPageFilters", mainPageFilters);

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
//        TODO rework context setVariable calls -- to make sure that context is set only for filtered values
        context.setVariable("categories", categories);
        context.setVariable("suppliers", suppliers);
        context.setVariable("products", products);
        if (session != null) {
            context.setVariable("username", session.getAttribute("username"));
        }
        engine.process("product/index.html", context, resp.getWriter());

    }

    private void setAllProductVisible(ProductDao productDataStore) {
        for (int i = 0; i < productDataStore.getAll().size(); i++) {
            Product product = productDataStore.getAll().get(i);
            product.setVisibility(true);
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
