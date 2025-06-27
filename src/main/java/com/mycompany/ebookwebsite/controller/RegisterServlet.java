/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import java.io.IOException;
import java.time.LocalDate;
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

        // Validate input
        if (username == null || password == null || email == null ||
                username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();

        try {
            // Kiểm tra username/email đã tồn tại chưa
            if (userDAO.findByUsername(username) != null) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
                return;
            }
            if (userDAO.findByEmail(email) != null) {
                request.setAttribute("error", "Email đã được sử dụng!");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
                return;
            }

            // Tạo user mới, role mặc định là "user"
            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(password); // Password sẽ được hash trong UserService
            user.setEmail(email);
            user.setRole("user");
            user.setAvatarUrl("/assets/img/default-avatar.png");
            user.setStatus("active");
            user.setCreatedAt(LocalDate.now());

            // Sử dụng UserService để tạo user (sẽ hash password)
            com.mycompany.ebookwebsite.service.UserService userService = new com.mycompany.ebookwebsite.service.UserService();
            userService.createUser(user);

            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Đăng ký thất bại, thử lại sau!");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Register Servlet";
    }
}
