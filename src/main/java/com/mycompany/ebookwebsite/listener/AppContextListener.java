/*
 * AppContextListener – quản lý sự kiện START/STOP của toàn bộ web-app
 * © 2025 EbookWebsite
 */
package com.mycompany.ebookwebsite.listener;

import com.mycompany.ebookwebsite.dao.DBConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener               // container tự động quét & đăng ký
public class AppContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppContextListener.class.getName());
    private Connection testConn;      // kết nối test (tuỳ chọn)

    /**
     * Hàm được gọi MỘT LẦN duy nhất khi ứng dụng khởi động
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        LOG.info(">>> EbookWebsite STARTING ...");

        /* 1) (Tuỳ chọn) nạp driver JDBC thủ công – an toàn với mọi container   */
        try {
            Class.forName(DBConnection.driverName);
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "Không tìm thấy driver JDBC!", e);
        }

        /* 2) (Tuỳ chọn) kiểm tra kết nối DB ngay khi khởi động                */
        try {
            testConn = DBConnection.getConnection();
            if (testConn != null && !testConn.isClosed()) {
                LOG.info("Kết nối CSDL thành công khi khởi động ứng dụng!");
            } else {
                LOG.warning("Không thể kết nối CSDL khi khởi động!");
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Lỗi kết nối CSDL khi khởi động!", ex);
        }

        /* 3) Khởi tạo biến toàn cục dùng chung – ví dụ đếm user online        */
        ctx.setAttribute("onlineUsers", 0);

        /* 4) Bạn có thể khởi tạo thêm:
           – Connection-Pool (HikariCP, DBCP…)
           – Scheduler (Quartz) cho tác vụ định kỳ
           – Đọc file cấu hình ngoài (properties, YAML…)  */

        LOG.info(">>> EbookWebsite INITIALIZED thành công!");
    }

    /**
     * Hàm được gọi MỘT LẦN duy nhất khi ứng dụng shutdown
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info(">>> EbookWebsite SHUTTING DOWN ...");

        /* Đóng kết nối test (nếu đã mở) */
        if (testConn != null) {
            try {
                testConn.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Không thể đóng kết nối CSDL!", ex);
            }
        }

        /* Đóng/huỷ các tài nguyên khác mà bạn mở ở contextInitialized */

        LOG.info(">>> EbookWebsite STOPPED hoàn tất!");
    }
}
