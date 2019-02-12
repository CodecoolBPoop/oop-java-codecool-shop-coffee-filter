package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.AddressHandler;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.AddressHandlerSQL;
import com.codecool.shop.dao.implementation.OrderDaoSQL;
import com.codecool.shop.dao.implementation.UserDaoSQL;
import com.codecool.shop.model.Address;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Session
        HttpSession session = req.getSession(false);

        AddressHandler addressDataStore = AddressHandlerSQL.getInstance();
        UserDao userDataStore = UserDaoSQL.getInstance();
        OrderDao orderDataStore = OrderDaoSQL.getInstance();


        int userId = Integer.valueOf(session.getAttribute("userId").toString());
        User user = userDataStore.getUserById(userId);

        Order order = orderDataStore.getLatestUnfinishedOrderByUser(userId);
        int deliveryAddressId = order.getDeliveryAddressId();

        Address address = addressDataStore.getAddressById(deliveryAddressId);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        float amountToPay = order.getAmountToPay();
        String name = address.getFirstName() + " " + address.getLastName();
        // todo: use string builder, check if state, storey, door are not null and format accordingly
        String addressLine1 = address.getCountry() + " "
                + address.getPostalCode() + ", "
                + address.getState();
        String addressLine2 = address.getCity() + " "
                + address.getStreet() + " "
                + address.getHouseNumber() + " "
                + address.getStorey() + " "
                + address.getDoor();

        context.setVariable("amountToPay", amountToPay);
        context.setVariable("email", userId);
        context.setVariable("name", name);
        context.setVariable("address1", addressLine1);
        context.setVariable("address2", addressLine2);
        if (session != null) {
            context.setVariable("username", session.getAttribute("username"));
            context.setVariable("email", user.getEmail());
        }

        engine.process("payment/payment.html", context, resp.getWriter());

    }
}
