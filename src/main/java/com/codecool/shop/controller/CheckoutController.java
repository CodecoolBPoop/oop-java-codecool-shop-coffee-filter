package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.AddressHandler;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.AddressHandlerSQL;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // todo: check if userId in session, if not: redirect to registration or login
        // Session
        HttpSession session = req.getSession();
        Integer userIdInSession = (Integer) session.getAttribute("userId");
        int userId = userIdInSession != null ? userIdInSession : 0;

        if (userId == 0) {
            session.setAttribute("redirectTo", Redirect.CHECKOUT);
            resp.sendRedirect("/login");
        }

        OrderDao orderDataStore = OrderDaoSQL.getInstance();
        Order order = orderDataStore.getLatestUnfinishedOrderByUser(userId);
        HashMap<Integer, LineItem> shoppingCart = order.getShoppingCart();
        float amountToPay = order.getAmountToPay();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("cart", order);
        context.setVariable("shoppingcart", shoppingCart);
        context.setVariable("amountToPay", amountToPay);
        String username = (String) session.getAttribute("username");

        if (username != null) {
            context.setVariable("username", username);
        }

        engine.process("checkout/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddressHandler addressDataStore = AddressHandlerSQL.getInstance();
        OrderDao orderDataStore = OrderDaoSQL.getInstance();

        int userId = Integer.valueOf(req.getSession().getAttribute("userId").toString());
        String country = req.getParameter("country");
        String state = req.getParameter("state");
        String postalCode = req.getParameter("zip");
        String city = req.getParameter("city");
        String street = req.getParameter("street");
        String houseNumber = req.getParameter("house-number");
        String storey = req.getParameter("storey");
        String door = req.getParameter("door");
        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");
        int orderId = orderDataStore.getLatestUnfinishedOrderIdByUser(userId);

        addressDataStore.add(country, state, postalCode, city, street, houseNumber, storey, door, firstName, lastName, orderId);

        resp.sendRedirect("/payment");
    }
}