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
import java.io.IOException;
import java.util.Arrays;

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

        boolean usernameIsValid = isValidUsername(username);
        boolean passwordIsValid = isValidPassword(password);
        boolean emailIsValid = isValidEmailAddress(email);

        UserDaoSQL uds = UserDaoSQL.getInstance();

        if (uds.checkIsExists(username, email) && usernameIsValid && passwordIsValid  && emailIsValid) {
            uds.add(username, password, email);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/invalid_registration");
        }

    }

    private boolean isValidUsername(String username) {
        boolean valid = true;
        if (username.length() < 4 || username.length() > 20) {
            valid = false;
        }

        String [] prohibitedUsernames = {"admin", "superuser"};
        if (Arrays.asList(prohibitedUsernames).contains(username)) {
            valid = false;
        }

        return valid;
    }

    private boolean isValidPassword(String password) {
        boolean valid = true;

        if (password.length() < 8 || password.length() > 15) {
            valid = false;
        }

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars )) {
            valid = false;
        }

        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars )) {
            valid = false;
        }

        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers )) {
            valid = false;
        }

        return valid;
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
}
