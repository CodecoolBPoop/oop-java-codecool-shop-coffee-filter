package com.codecool.shop.config;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        ProductDao productDataStore = ProductDaoMem.getInstance();
//        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
//        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
//        OrderDao orderDataStore = OrderDaoMem.getInstance();
//        int userId = 1;
//
//        //setting up a new supplier
//        Supplier nespresso = new Supplier("Nespresso", "Coffee Machine");
//        supplierDataStore.add(nespresso);
//        Supplier keepcup = new Supplier("KeepCup", "Portable cups");
//        supplierDataStore.add(keepcup);
//        Supplier bialetti = new Supplier("Bialetti", "");
//        supplierDataStore.add(bialetti);
//        Supplier hario = new Supplier("Hario", "");
//        supplierDataStore.add(hario);
//
//        //setting up a new product category
//        ProductCategory coffeemachine = new ProductCategory("Coffee Machine", "Hardware", "Machines for making coffee.");
//        productCategoryDataStore.add(coffeemachine);
//
//        ProductCategory cup = new ProductCategory("Cup", "Cups", "");
//        productCategoryDataStore.add(cup);
//
//        ProductCategory accessories = new ProductCategory("Accessories", "Alternative", "Accessories for speciality coffee.");
//        productCategoryDataStore.add(accessories);
//
//
//        //setting up products and printing it
//        productDataStore.add(new Product("Nespresso Krups XN740B CitiZ", 120, "USD", "Kapsule coffee machine.", coffeemachine, nespresso));
//        productDataStore.add(new Product("KeepCup Tall", 10, "USD", "Ideal for take away coffee.", cup, keepcup));
//        productDataStore.add(new Product("KeepCup Medium", 8, "USD", "Ideal for take away coffee.", cup, keepcup));
//        productDataStore.add(new Product("Bialetti Moka pot", 15, "USD", "Old time classic.", accessories, bialetti));
//        productDataStore.add(new Product("Hario Chemex", 25, "USD", "Coolest way to do filter coffee.", accessories, hario));
//        productDataStore.add(new Product("Hario Manual Grinder", 17, "USD", "Fine grinder for your favourite coffee.", accessories, hario));
//
//        //setting up a new order
//        Order order = new Order(1, productDataStore.find(1));
//        order.addProductToCart(productDataStore.find(2));
//        order.addProductToCart(productDataStore.find(1));
//        orderDataStore.add(1, order );
    }
}
