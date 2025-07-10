package com.mycompany.ebookwebsite.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.service.EbookWithAIService;
import com.mycompany.ebookwebsite.service.EbookWithAIService.EbookWithAI;
import com.mycompany.ebookwebsite.model.BookMetadata;
import com.mycompany.ebookwebsite.model.BookStatus;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.OpenAIContentFilterService;
import com.mycompany.ebookwebsite.service.EnhancedRAGService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "BookServlet", urlPatterns = {"/book/*"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class BookServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BookServlet.class);
    private EbookDAO ebookDAO;
    private EbookWithAIService ebookWithAIService;
    private OpenAIContentFilterService contentFilterService;
    private EnhancedRAGService ragService;

    // Debug mode configuration
    private static final boolean DEBUG_MODE = true;
    private static final String UPLOAD_FOLDER = "uploads";
    
    @Override
    public void init() throws ServletException {
        try {
            ebookDAO = new EbookDAO();
            ebookWithAIService = new EbookWithAIService();
            contentFilterService = new OpenAIContentFilterService();
            ragService = new EnhancedRAGService();
            logger.info("✅ BookServlet initialized with LangChain4j RAG Service");
            
            // Tạo thư mục uploads nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_FOLDER);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
        } catch (Exception e) {
            logger.error("❌ Failed to initialize BookServlet: " + e.getMessage(), e);
            throw new ServletException("Failed to initialize Book system", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        logger.info("📥 BookServlet.doGet - action: " + action);
        
        try {
            switch (action) {
                case "list":
                    showBookList(request, response);
                    break;


                case "view":
                    showBookDetails(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "editSummary":
                    showEditSummaryForm(request, response);
                    break;
                case "delete":
                    showDeleteConfirm(request, response);
                    break;
                case "search":
                    searchBooks(request, response);
                    break;
                default:
                    showBookList(request, response);
            }
        } catch (SQLException e) {
            logger.error("❌ Database error in doGet: " + e.getMessage(), e);
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        } catch (Exception e) {
            logger.error("❌ Unexpected error in doGet: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi không mong muốn: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        logger.info("📤 BookServlet.doPost - action: " + action);
        
        try {
            switch (action != null ? action : "") {
                case "edit":
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chức năng cập nhật sách đã bị loại bỏ");
                    break;
                case "create":
                    createBook(request, response);
                    break;
                case "editSummary":
                    updateSummary(request, response);
                    break;
                case "delete":
                    deleteBook(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    break;
            }
        } catch (Exception e) {
            logger.error("❌ Error in doPost: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Có lỗi khi xử lý: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }

    // ========== SAFE FORWARD METHOD ==========
    private void safeForward(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (Exception e) {
            logger.error("❌ Failed to forward to " + path + ": " + e.getMessage(), e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Lỗi hệ thống: Không thể hiển thị trang " + path);
            } catch (IOException ioEx) {
                logger.error("❌ Failed to send error response: " + ioEx.getMessage(), ioEx);
            }
        }
    }

    // ========== GET Methods ==========

    private void showBookList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("📋 Showing book list");
        try {
            List<Ebook> bookList = ebookDAO.selectAllEbooks();
            request.setAttribute("bookList", bookList);
            request.setAttribute("totalBooks", bookList.size());
            logger.info("✅ Found " + bookList.size() + " books");
            
            // ✅ SỬA: Đường dẫn đúng tới list.jsp
            safeForward(request, response, "/book/list.jsp");
        } catch (SQLException e) {
            logger.error("❌ Database error in showBookList: " + e.getMessage(), e);
            request.setAttribute("error", "Không thể tải danh sách sách: " + e.getMessage());
            request.setAttribute("bookList", java.util.Collections.emptyList());
            request.setAttribute("totalBooks", 0);
            safeForward(request, response, "/book/list.jsp");
        }
    }



    private void showBookDetails(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect("book?action=list");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(idParam);
            Ebook book = ebookDAO.selectEbook(bookId);
            
            if (book == null) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + bookId);
                safeForward(request, response, "/book/list.jsp");
                return;
            }
            
            request.setAttribute("book", book);
            safeForward(request, response, "/book/detail.jsp");
            
        } catch (NumberFormatException e) {
            logger.error("❌ Invalid book ID: " + idParam, e);
            request.setAttribute("error", "ID sách không hợp lệ");
            safeForward(request, response, "/book/list.jsp");
        } catch (Exception e) {
            logger.error("❌ Error in showBookDetails: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi khi tải thông tin sách: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        safeForward(request, response, "/book/bookForm.jsp");
    }

    private void showEditSummaryForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            EbookWithAI book = ebookWithAIService.getEbookWithAI(id);
            if (book == null) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + id);
                safeForward(request, response, "/book/list.jsp");
                return;
            }
            request.setAttribute("book", book);
            safeForward(request, response, "/book/editSummary.jsp");
        } catch (NumberFormatException e) {
            logger.error("❌ Invalid book ID: " + request.getParameter("id"), e);
            request.setAttribute("error", "ID sách không hợp lệ");
            safeForward(request, response, "/book/list.jsp");
        }
    }

    private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Ebook book = ebookDAO.selectEbook(id);
            if (book == null) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + id);
                safeForward(request, response, "/book/list.jsp");
                return;
            }
            request.setAttribute("book", book);
            safeForward(request, response, "/book/bookDeleteConfirm.jsp");
        } catch (NumberFormatException e) {
            logger.error("❌ Invalid book ID: " + request.getParameter("id"), e);
            request.setAttribute("error", "ID sách không hợp lệ");
            safeForward(request, response, "/book/list.jsp");
        }
    }

    private void searchBooks(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            String searchTitle = request.getParameter("searchTitle");
            List<Ebook> bookList = ebookDAO.search(searchTitle);
            request.setAttribute("bookList", bookList);
            request.setAttribute("searchTitle", searchTitle);
            request.setAttribute("totalBooks", bookList.size());
            safeForward(request, response, "/book/list.jsp");
        } catch (Exception e) {
            logger.error("❌ Error in searchBooks: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi khi tìm kiếm: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }

    // ========== POST Methods ==========

    private void createBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            // Implementation với EbookDAO...
            logger.info("➕ Creating new book");
            response.sendRedirect("book?action=list");
        } catch (Exception e) {
            logger.error("❌ Error in createBook: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi khi tạo sách: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }
    
    private void updateSummary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String summary = request.getParameter("summary");
            
            if (summary == null || summary.trim().isEmpty()) {
                request.setAttribute("error", "Nội dung tóm tắt không được để trống");
                Ebook book = ebookDAO.selectEbook(id);
                request.setAttribute("book", book);
                safeForward(request, response, "/book/editSummary.jsp");
                return;
            }
            
            // Use EbookWithAIService to update summary
            boolean success = ebookWithAIService.updateSummary(id, summary.trim());
            if (!success) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + id);
                safeForward(request, response, "/book/list.jsp");
                return;
            }
            
            logger.info("📝 Updated summary for book ID: " + id);
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + id);
        } catch (NumberFormatException e) {
            logger.error("❌ Invalid book ID: " + request.getParameter("id"), e);
            request.setAttribute("error", "ID sách không hợp lệ");
            safeForward(request, response, "/book/list.jsp");
        } catch (Exception e) {
            logger.error("❌ Error in updateSummary: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi khi cập nhật tóm tắt: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ebookDAO.deleteEbook(id);
            logger.info("🗑️ Deleted book with ID: " + id);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (NumberFormatException e) {
            logger.error("❌ Invalid book ID for deletion: " + request.getParameter("id"), e);
            request.setAttribute("error", "ID sách không hợp lệ");
            safeForward(request, response, "/book/list.jsp");
        } catch (Exception e) {
            logger.error("❌ Error in deleteBook: " + e.getMessage(), e);
            request.setAttribute("error", "Có lỗi khi xóa sách: " + e.getMessage());
            safeForward(request, response, "/book/list.jsp");
        }
    }
} 
