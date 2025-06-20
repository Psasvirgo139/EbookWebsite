package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserInfor;
import com.mycompany.ebookwebsite.service.UserService;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin chi tiết UserInfor nếu có userinforId
        // Lấy thông tin chi tiết UserInfor nếu có userinforId
        UserInfor userInfor = null;
        if (user.getUserinforId() != null) {
            userInfor = userService.getUserInforById(user.getUserinforId());
        }

// Format ngày tạo thành String
        String createdAtStr = "-";
        if (user.getCreatedAt() != null) {
            createdAtStr = user.getCreatedAt()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        request.setAttribute("createdAtStr", createdAtStr);

// Format ngày sinh
        String birthDayStr = "-";
        if (userInfor != null && userInfor.getBirthDay() != null) {
            birthDayStr = userInfor.getBirthDay()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        request.setAttribute("birthDayStr", birthDayStr);

        request.setAttribute("user", user);
        request.setAttribute("userInfor", userInfor);
        request.getRequestDispatcher("user/profile.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet hiển thị trang cá nhân người dùng";
    }
}
