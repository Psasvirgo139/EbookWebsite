/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.UserValidation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Validate dữ liệu đầu vào
        try {
            UserValidation.validateUsername(username);
            UserValidation.validatePassword(password);
            UserValidation.validateEmail(email);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        try {
            // Kiểm tra username/email đã tồn tại chưa
            if (userService.isUsernameExists(username, 0)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
                return;
            }
            if (userService.isEmailExists(email, 0)) {
                request.setAttribute("error", "Email đã được sử dụng!");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
                return;
            }

            // Tạo user mới
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(password); // Password sẽ được hash trong UserService
            user.setEmail(email);
            user.setRole("user");
            user.setAvatarUrl("/assets/img/default-avatar.png");
            user.setStatus("active");
            user.setCreatedAt(LocalDate.now());

            // Validate user data
            UserValidation.validateUserData(user, true);

            // Sử dụng UserService để tạo user
            userService.createUser(user);

            response.sendRedirect(request.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại, thử lại sau!");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Register Servlet";
    }
}
