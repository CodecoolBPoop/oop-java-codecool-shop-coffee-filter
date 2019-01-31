package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.AddressSavingDaoSQL;
import com.codecool.shop.dao.implementation.OrderDaoSQL;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDao orderDataStore = OrderDaoSQL.getInstance();
        Order order = orderDataStore.getLatestUnfinishedOrderByUser(1);
        HashMap<Integer, LineItem> shoppingCart = order.getShoppingCart();
        float amountToPay = order.getAmountToPay();


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("cart", order);
        context.setVariable("shoppingcart", shoppingCart);
        context.setVariable("amountToPay", amountToPay);

        engine.process("checkout/checkout.html", context, resp.getWriter());


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddressSavingDaoSQL addressSavingDaoSQL = AddressSavingDaoSQL.getInstance();

        String country = req.getParameter("country");
        String state = req.getParameter("state");
        String postalCode = req.getParameter("zip");
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        String houseNumber = req.getParameter("house-number");
        String story = req.getParameter("story");
        String door = req.getParameter("door");
        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");

        addressSavingDaoSQL.add(country, state, postalCode, city, street, houseNumber, story, door, firstName, lastName);

        resp.sendRedirect("/payment");
    }
}