package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserInforDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserInfor;
import com.mycompany.ebookwebsite.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    private UserService userService;
    private UserDAO userDAO;
    private UserInforDAO userInforDAO;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        userDAO = new UserDAO();
        userInforDAO = new UserInforDAO();
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

        // Xử lý thông báo từ URL parameters
        String message = request.getParameter("message");
        String messageType = request.getParameter("type");
        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
        }
        
        // Xử lý thông báo từ session (từ ChangePasswordServlet)
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("message", successMessage);
            request.setAttribute("messageType", "success");
            // Xóa thông báo khỏi session để không hiển thị lại
            session.removeAttribute("successMessage");
        }

        request.setAttribute("user", user);
        request.setAttribute("userInfor", userInfor);
        request.getRequestDispatcher("user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String message = "";
        String messageType = "success";

        try {
            // Lấy thông tin từ form
            String phone = request.getParameter("phone");
            String birthdayStr = request.getParameter("birthday");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String introduction = request.getParameter("introduction");
            String avatarUrl = request.getParameter("avatar_url");

            // Validate dữ liệu
            if (phone != null && phone.length() > 20) {
                throw new IllegalArgumentException("Số điện thoại không được quá 20 ký tự");
            }
            if (address != null && address.length() > 200) {
                throw new IllegalArgumentException("Địa chỉ không được quá 200 ký tự");
            }
            if (introduction != null && introduction.length() > 500) {
                throw new IllegalArgumentException("Giới thiệu không được quá 500 ký tự");
            }

            // Parse ngày sinh
            LocalDate birthday = null;
            if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
                try {
                    birthday = LocalDate.parse(birthdayStr);
                    // Kiểm tra ngày sinh hợp lệ
                    if (birthday.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("Ngày sinh không thể là ngày trong tương lai");
                    }
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Ngày sinh không đúng định dạng");
                }
            }

            // Cập nhật User
            User userToUpdate = new User();
            userToUpdate.setId(currentUser.getId());
            userToUpdate.setUsername(currentUser.getUsername());
            userToUpdate.setEmail(currentUser.getEmail());
            userToUpdate.setPasswordHash(currentUser.getPasswordHash());
            userToUpdate.setAvatarUrl(avatarUrl != null ? avatarUrl.trim() : currentUser.getAvatarUrl());
            userToUpdate.setRole(currentUser.getRole());
            userToUpdate.setCreatedAt(currentUser.getCreatedAt());
            userToUpdate.setUserinforId(currentUser.getUserinforId());
            userToUpdate.setStatus(currentUser.getStatus());
            userToUpdate.setLastLogin(currentUser.getLastLogin());

            // Cập nhật hoặc tạo UserInfor
            UserInfor userInfor = null;
            if (currentUser.getUserinforId() != null) {
                // Cập nhật UserInfor hiện có
                userInfor = userInforDAO.selectUserInfor(currentUser.getUserinforId());
                if (userInfor == null) {
                    // Nếu không tìm thấy, tạo mới
                    userInfor = new UserInfor();
                }
            } else {
                // Tạo mới UserInfor
                userInfor = new UserInfor();
            }

            // Cập nhật thông tin UserInfor
            userInfor.setPhone(phone != null ? phone.trim() : "");
            userInfor.setBirthDay(birthday);
            userInfor.setGender(gender != null ? gender.trim() : "");
            userInfor.setAddress(address != null ? address.trim() : "");
            userInfor.setIntroduction(introduction != null ? introduction.trim() : "");

            // Lưu vào database
            boolean success = false;
            if (userInfor.getId() == 0) {
                // Tạo mới UserInfor
                LOGGER.info("Creating new UserInfor for user: " + currentUser.getUsername());
                userInforDAO.insertUserInfor(userInfor);
                LOGGER.info("Created UserInfor with ID: " + userInfor.getId());
                
                // Cập nhật userinforId trong User
                userToUpdate.setUserinforId(userInfor.getId());
                LOGGER.info("Updating User with userinforId: " + userInfor.getId());
                success = userDAO.update(userToUpdate);
            } else {
                // Cập nhật UserInfor hiện có
                LOGGER.info("Updating existing UserInfor with ID: " + userInfor.getId());
                boolean userInforUpdated = userInforDAO.updateUserInfor(userInfor);
                boolean userUpdated = userDAO.update(userToUpdate);
                success = userInforUpdated && userUpdated;
                LOGGER.info("UserInfor updated: " + userInforUpdated + ", User updated: " + userUpdated);
            }

            if (success) {
                // Cập nhật session
                session.setAttribute("user", userToUpdate);
                LOGGER.info("Profile updated successfully for user: " + currentUser.getUsername());
                message = "Cập nhật thông tin thành công!";
                messageType = "success";
            } else {
                LOGGER.warning("Failed to update profile for user: " + currentUser.getUsername());
                message = "Có lỗi xảy ra khi cập nhật thông tin!";
                messageType = "error";
            }

        } catch (IllegalArgumentException e) {
            LOGGER.warning("Validation error: " + e.getMessage());
            message = e.getMessage();
            messageType = "error";
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            message = "Lỗi database: " + e.getMessage();
            messageType = "error";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            message = "Có lỗi xảy ra: " + e.getMessage();
            messageType = "error";
        }

        // Redirect về trang profile với thông báo
        response.sendRedirect(request.getContextPath() + "/profile?message=" + 
                            java.net.URLEncoder.encode(message, "UTF-8") + 
                            "&type=" + messageType);
    }

    @Override
    public String getServletInfo() {
        return "Servlet hiển thị và cập nhật trang cá nhân người dùng";
    }
}
