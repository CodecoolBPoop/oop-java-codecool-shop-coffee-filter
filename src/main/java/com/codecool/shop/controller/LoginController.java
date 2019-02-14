package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoSQL;
import com.codecool.shop.dao.implementation.UserDaoSQL;
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

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        String jumbotronText = "Login";
        context.setVariable("jumbotronText", jumbotronText);
        engine.process("session/login.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        OrderDao orderDataStore = OrderDaoSQL.getInstance();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDaoSQL uds = UserDaoSQL.getInstance();
        if (uds.checkNameAndPassword(username, password)) {
            int userId = uds.getUserIdByUsername(username);
            String redirectTo = "/";
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                Order temporaryOrder = (Order) oldSession.getAttribute("temporaryOrder");
                if (temporaryOrder != null) {
                    orderDataStore.addTemporaryCartToUserOrder(temporaryOrder, userId);
                    oldSession.removeAttribute("temporaryOrder");
                }
                if (Redirect.CHECKOUT.equals(oldSession.getAttribute("redirectTo"))) {
                    redirectTo = "/checkout";
                }
                oldSession.invalidate();
            }
            HttpSession newSession = req.getSession();
            newSession.setAttribute("username", username);
            newSession.setAttribute("userId", userId);
            resp.sendRedirect(redirectTo);
        } else {
            context.setVariable("wrongLogin", "Wrong user name or password!");
            engine.process("session/login.html", context, resp.getWriter());
        }
    }
}