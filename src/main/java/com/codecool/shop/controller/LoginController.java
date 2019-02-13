package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.UserDaoSQL;
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

        engine.process("session/login.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDaoSQL uds = UserDaoSQL.getInstance();
        if (uds.checkNameAndPassword(username, password)) {
            // todo: if items in session, add to user's cart - if came from checkout, throw back to checkout (put checkout to session?)
            HttpSession session = req.getSession();
            int userId = uds.getUserIdByUsername(username);
            session.setAttribute("username", username);
            session.setAttribute("userId", userId);
            resp.sendRedirect("/");
        } else {
            context.setVariable("wrongLogin", "Wrong user name or password!");
            engine.process("session/login.html", context, resp.getWriter());
        }
    }
}