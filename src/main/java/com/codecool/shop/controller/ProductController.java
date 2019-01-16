package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        Map mainPageFilters = new HashMap<>();

        //Filters
        String category = req.getParameter("category");
        String supplier = req.getParameter("supplier");

        //If there are no filters or both are set to All
        if ((category == null || Integer.parseInt(category) == 0) && (supplier == null || Integer.parseInt(supplier) == 0)) {
            for (int i = 0; i < productDataStore.getAll().size(); i++) {
                Product product = productDataStore.getAll().get(i);
                product.setVisibilityCategory(true);
                product.setVisibilitySupplier(true);
                product.setVisibility();
            }
        }
        //If the both filters are set
        else if ((category != null || Integer.parseInt(category) != 0) && (supplier != null || Integer.parseInt(supplier) != 0)) {
            mainPageFilters.put("filteredCategory", Integer.parseInt(category));
            mainPageFilters.put("filteredSupplier", Integer.parseInt(supplier));
            for (int i = 0; i < productDataStore.getAll().size(); i++) {
                Product product = productDataStore.getAll().get(i);
                if (product.getProductCategory().getId() != Integer.parseInt(category) && product.getSupplier().getId() != Integer.parseInt(supplier)) {
                    product.setVisibilityCategory(false);
                    product.setVisibilitySupplier(false);
                }
                product.setVisibility();
            }
        }
        //If Category is set but Supplier is not set
        else if (category != null || Integer.parseInt(category) != 0) {
            mainPageFilters.put("filteredCategory", Integer.parseInt(category));
            for (int i = 0; i < productDataStore.getAll().size(); i++) {
                Product product = productDataStore.getAll().get(i);
                product.setVisibilitySupplier(true);
                if (product.getProductCategory().getId() != Integer.parseInt(category)) {
                    product.setVisibilityCategory(false);
                } else {
                    product.setVisibilityCategory(true);
                }
                product.setVisibility();
            }
        }
        //If Supplier is set but Category is not set
        else if (supplier != null || Integer.parseInt(supplier) != 0) {
            mainPageFilters.put("filteredSupplier", Integer.parseInt(supplier));
            for (int i = 0; i < productDataStore.getAll().size(); i++) {
                Product product = productDataStore.getAll().get(i);
                product.setVisibilityCategory(true);
                if (product.getSupplier().getId() != Integer.parseInt(supplier)) {
                    product.setVisibilitySupplier(false);
                } else {
                    product.setVisibilitySupplier(true);
                }
                product.setVisibility();
            }
        }


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("mainPageFilters", mainPageFilters);
        context.setVariable("recipient", "World");
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("products", productDataStore.getAll());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
