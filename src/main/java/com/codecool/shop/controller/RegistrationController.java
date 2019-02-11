package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.UserDaoSQL;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/registration"})
public class RegistrationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        engine.process("session/registration.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        boolean emailIsValid = isValidEmailAddress(email);
        boolean passwordIsValid = isValidPassword(password);

        UserDaoSQL uds = UserDaoSQL.getInstance();
        if (uds.checkNameAndEmail(username, email) && emailIsValid && passwordIsValid) {
            uds.add(username, password, email);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/invalid_registration");
        }

    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isValidPassword(String password) {
        boolean valid = true;
        // check password length
        if (password.length() < 8 || password.length() > 15) {
            valid = false;
        }
        // check password includes the upper case element
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars )) {
            valid = false;
        }
        // check password includes the small case element
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars )) {
            valid = false;
        }
        // check password includes the number
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers )) {
            valid = false;
        }
        return valid;
    }
}
