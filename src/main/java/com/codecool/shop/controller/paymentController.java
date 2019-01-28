package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
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

@WebServlet(urlPatterns = {"/payment"})
public class paymentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getLatestUnfinishedOrderByUser(1);
        HashMap<Integer, LineItem> shoppingCart = order.getShoppingCart();
        float amountToPay = order.getAmountToPay();

        String billingFirstName = req.getParameter("firstname");
        String billingLastName = req.getParameter("lastname");
        String billingEmail = req.getParameter("email");
        String billingAddress = req.getParameter("address");
        String billingCity = req.getParameter("city");
        String billingCountry = req.getParameter("country");
        String billingZip = req.getParameter("zip");


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("billingFirstname", billingFirstName);
        context.setVariable("billingLastname", billingLastName);
        context.setVariable("billingEmail", billingEmail);
        context.setVariable("billingAddress", billingAddress);
        context.setVariable("billingCity", billingCity);
        context.setVariable("billingCountry", billingCountry);
        context.setVariable("billingZip", billingZip);

        context.setVariable("amountToPay", amountToPay);


        engine.process("payment/payment.html", context, resp.getWriter());

    }
}
