package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.EbookAIDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.EbookAI;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.LangChain4jAIChatService;
import com.mycompany.ebookwebsite.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/book/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class BookUploadServlet extends HttpServlet {

    private EbookDAO ebookDAO;
    private EbookAIDAO ebookAIDAO;
    private LangChain4jAIChatService aiService;
    private static final String UPLOADS_FOLDER = "D:\\EbookWebsite\\uploads";

    @Override
    public void init() throws ServletException {
        ebookDAO = new EbookDAO();
        ebookAIDAO = new EbookAIDAO();
        aiService = new LangChain4jAIChatService();
        
        // Tạo thư mục uploads nếu chưa có
        File uploadsDir = new File(UPLOADS_FOLDER);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
            System.out.println("📁 Created uploads directory: " + UPLOADS_FOLDER);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // DEBUG: Log incoming request
        System.out.println("🔍 BookUploadServlet.doGet() called");
        System.out.println("🔍 Request URI: " + request.getRequestURI());
        System.out.println("🔍 Context Path: " + request.getContextPath());
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        System.out.println("🔍 Session: " + (session != null ? session.getId() : "NULL"));
        System.out.println("🔍 Session isNew: " + (session != null ? session.isNew() : "N/A"));
        
        Object userObj = session != null ? session.getAttribute("user") : null;
        System.out.println("🔍 User object: " + (userObj != null ? userObj.getClass().getSimpleName() : "NULL"));
        
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("❌ Authentication failed - redirecting to login");
            System.out.println("❌ Redirect URL: " + request.getContextPath() + "/user/login.jsp");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        System.out.println("✅ Authentication successful - USING ENHANCED DIRECT HTML OUTPUT");
        
        // Enhanced direct HTML output with title input
        response.setContentType("text/html; charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();
        
        User user = (User) session.getAttribute("user");
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>🚀 Upload sách với AI</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println(".container { max-width: 900px; margin: 0 auto; padding: 20px; }");
        out.println(".header { background: rgba(255,255,255,0.95); padding: 25px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 8px 32px rgba(0,0,0,0.1); }");
        out.println(".upload-section { background: rgba(255,255,255,0.98); padding: 30px; border-radius: 15px; margin-bottom: 25px; box-shadow: 0 8px 32px rgba(0,0,0,0.1); }");
        out.println(".title-section { background: linear-gradient(135deg, #e8f5e8, #d4edda); padding: 25px; border-radius: 12px; margin-bottom: 25px; border: 2px solid #27ae60; }");
        out.println(".file-zone { border: 3px dashed #3498db; padding: 40px; text-align: center; border-radius: 12px; background: #f8f9fa; margin: 20px 0; transition: all 0.3s ease; }");
        out.println(".file-zone:hover { border-color: #2980b9; background: #e8f4f8; transform: translateY(-2px); }");
        out.println(".btn { background: linear-gradient(135deg, #3498db, #2980b9); color: white; padding: 15px 30px; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; font-weight: 600; transition: all 0.3s ease; margin: 10px; }");
        out.println(".btn:hover { transform: translateY(-2px); box-shadow: 0 8px 25px rgba(52, 152, 219, 0.3); }");
        out.println(".btn-success { background: linear-gradient(135deg, #27ae60, #229954); }");
        out.println(".btn-success:hover { box-shadow: 0 8px 25px rgba(39, 174, 96, 0.3); }");
        out.println(".input-field { width: 100%; padding: 15px; border: 2px solid #27ae60; border-radius: 8px; font-size: 16px; box-sizing: border-box; margin-bottom: 10px; }");
        out.println(".input-field:focus { outline: none; border-color: #2ecc71; box-shadow: 0 0 0 3px rgba(46, 204, 113, 0.1); }");
        out.println(".ai-features { background: linear-gradient(135deg, #f8f9fa, #e9ecef); padding: 25px; border-radius: 12px; margin: 20px 0; }");
        out.println(".feature-list { margin: 0; padding-left: 20px; }");
        out.println(".feature-list li { margin-bottom: 8px; color: #2c3e50; }");
        out.println(".highlight { color: #e74c3c; font-weight: bold; }");
        out.println(".tip-box { background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; padding: 15px; border-radius: 8px; margin: 15px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<div class='container'>");
        
        out.println("<div class='header'>");
        out.println("<h1 style='color: #2c3e50; margin-bottom: 15px; text-align: center;'>🚀 Upload sách với AI - Enhanced</h1>");
        out.println("<p style='text-align: center; color: #7f8c8d; font-size: 18px;'>Nhập tên sách và upload file để AI tự động xử lý</p>");
        out.println("<div style='text-align: center; margin-top: 15px;'>");
        out.println("<span style='background: #e8f5e8; color: #27ae60; padding: 8px 16px; border-radius: 20px; font-weight: 600;'>👤 " + user.getUsername() + "</span>");
        out.println("<span style='background: #e3f2fd; color: #1976d2; padding: 8px 16px; border-radius: 20px; font-weight: 600; margin-left: 10px;'>🆔 " + user.getId() + "</span>");
        out.println("</div>");
        out.println("</div>");
        
        out.println("<form method='post' action='" + request.getContextPath() + "/book/upload' enctype='multipart/form-data'>");
        
        // Title section
        out.println("<div class='title-section'>");
        out.println("<h3 style='color: #27ae60; margin-bottom: 15px; display: flex; align-items: center;'>");
        out.println("<span style='font-size: 24px; margin-right: 10px;'>📝</span> Thông tin sách");
        out.println("</h3>");
        out.println("<label for='bookTitle' style='display: block; font-weight: bold; margin-bottom: 8px; color: #2c3e50;'>");
        out.println("🏷️ Tên sách của bạn:");
        out.println("</label>");
        out.println("<input type='text' id='bookTitle' name='bookTitle' class='input-field' ");
        out.println("placeholder='Nhập tên sách (VD: Harry Potter và Hòn đá Phù thủy)' required />");
        out.println("<p style='color: #7f8c8d; font-size: 14px; margin: 8px 0 0 0;'>");
        out.println("⭐ <strong>Quan trọng:</strong> Tên này sẽ được dùng làm tiêu đề chính của sách, không phụ thuộc vào AI extract");
        out.println("</p>");
        out.println("</div>");
        
        // File upload section
        out.println("<div class='upload-section'>");
        out.println("<h3 style='color: #3498db; margin-bottom: 15px; display: flex; align-items: center;'>");
        out.println("<span style='font-size: 24px; margin-right: 10px;'>📁</span> Upload File (Tùy chọn)");
        out.println("</h3>");
        
        out.println("<div class='file-zone'>");
        out.println("<div style='font-size: 48px; margin-bottom: 20px;'>📚</div>");
        out.println("<h3 style='color: #2c3e50; margin-bottom: 15px;'>Chọn file sách của bạn</h3>");
        out.println("<p style='color: #7f8c8d; margin-bottom: 20px;'>Hỗ trợ: .txt, .pdf, .docx | Tối đa 50MB</p>");
        out.println("<input type='file' name='bookFile' id='bookFile' accept='.txt,.pdf,.docx' ");
        out.println("style='margin-bottom: 15px;' />");
        out.println("</div>");
        
        out.println("<div class='tip-box'>");
        out.println("<strong>💡 Smart Tip:</strong> Nếu không chọn file, hệ thống sẽ tự động tìm file phù hợp với tên sách trong thư viện uploads");
        out.println("</div>");
        
        out.println("<div style='text-align: center; margin-top: 25px;'>");
        out.println("<button type='submit' class='btn btn-success' style='font-size: 18px; padding: 18px 40px;'>");
        out.println("🚀 Tạo sách với AI");
        out.println("</button>");
        out.println("</div>");
        out.println("</div>");
        
        out.println("</form>");
        
        // AI features section
        out.println("<div class='ai-features'>");
        out.println("<h3 style='color: #2c3e50; margin-bottom: 20px; display: flex; align-items: center;'>");
        out.println("<span style='font-size: 24px; margin-right: 10px;'>🤖</span> AI sẽ tự động xử lý:");
        out.println("</h3>");
        out.println("<ul class='feature-list'>");
        out.println("<li>✅ Sử dụng <span class='highlight'>tên bạn nhập</span> làm tiêu đề chính</li>");
        out.println("<li>✅ Tìm file phù hợp dựa trên tên sách (nếu không upload)</li>");
        out.println("<li>✅ Kiểm duyệt nội dung an toàn</li>");
        out.println("<li>✅ Phân loại thể loại chính xác</li>");
        out.println("<li>✅ Viết mô tả hấp dẫn</li>");
        out.println("<li>✅ Tạo tóm tắt thông minh</li>");
        out.println("<li>✅ Tự động tạo EbookAI record</li>");
        out.println("<li>✅ Đưa sách lên kệ nếu kiểm duyệt qua</li>");
        out.println("</ul>");
        out.println("</div>");
        
        out.println("<div style='background: rgba(255,255,255,0.9); padding: 20px; border-radius: 12px; margin: 20px 0; text-align: center;'>");
        out.println("<h4 style='color: #2c3e50; margin-bottom: 15px;'>🎯 Hai cách sử dụng:</h4>");
        out.println("<div style='display: flex; gap: 20px; justify-content: space-around; flex-wrap: wrap;'>");
        out.println("<div style='flex: 1; min-width: 300px; background: #e8f5e8; padding: 20px; border-radius: 8px; margin: 10px;'>");
        out.println("<h5 style='color: #27ae60;'>📝 Option 1: Chỉ nhập tên</h5>");
        out.println("<p>Nhập tên sách → Hệ thống tự tìm file phù hợp → Tạo sách</p>");
        out.println("</div>");
        out.println("<div style='flex: 1; min-width: 300px; background: #e3f2fd; padding: 20px; border-radius: 8px; margin: 10px;'>");
        out.println("<h5 style='color: #1976d2;'>📁 Option 2: Nhập tên + Upload</h5>");
        out.println("<p>Nhập tên sách + Upload file → AI xử lý → Tạo sách với tên bạn nhập</p>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // Lấy tên sách từ form
            String bookTitle = request.getParameter("bookTitle");
            if (bookTitle == null || bookTitle.trim().isEmpty()) {
                sendErrorResponse(response, "Vui lòng nhập tên sách");
                return;
            }
            bookTitle = bookTitle.trim();
            
            // Xử lý file upload
            Part filePart = request.getPart("bookFile");
            
            if (filePart == null || filePart.getSize() == 0) {
                // Không có file upload - tìm file dựa trên tên sách
                handleSmartFileSearch(request, response, user, bookTitle);
            } else {
                // Có file upload - xử lý với tên sách làm title
                handleFileUploadWithTitle(request, response, user, filePart, bookTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Có lỗi hệ thống: " + e.getMessage());
        }
    }

    private void handleMetadataOnlyUpload(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, SQLException {
        
            // Lấy thông tin từ form
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String releaseType = request.getParameter("releaseType");
            String language = request.getParameter("language");
            String status = request.getParameter("status");
            String visibility = request.getParameter("visibility");

            // Validate cơ bản
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Tiêu đề không được để trống");
                request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
                return;
            }

            // Tạo Ebook object
            Ebook ebook = new Ebook();
            ebook.setTitle(title.trim());
            ebook.setDescription(description != null ? description.trim() : "");
            ebook.setReleaseType(releaseType != null ? releaseType.trim() : "Khác");
            ebook.setLanguage(language != null ? language.trim() : "Tiếng Việt");
            ebook.setStatus(status != null ? status.trim() : "Đang ra");
            ebook.setVisibility(visibility != null ? visibility.trim() : "public");
            ebook.setUploaderId(user.getId());
            ebook.setViewCount(0);
            ebook.setCreatedAt(LocalDateTime.now());

            // Lưu vào database
            ebookDAO.insertEbook(ebook);

        // Redirect về trang chính với thông báo thành công
        response.sendRedirect(request.getContextPath() + "/?success=book_created&bookTitle=" + java.net.URLEncoder.encode(title, "UTF-8"));
    }

    private void handleFileUpload(HttpServletRequest request, HttpServletResponse response, User user, Part filePart)
            throws ServletException, IOException, SQLException {
        
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.trim().isEmpty()) {
            request.setAttribute("error", "Tên file không hợp lệ");
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // Kiểm tra định dạng file
        if (!isSupportedExtension(fileName)) {
            request.setAttribute("error", "Định dạng file không được hỗ trợ. Chỉ hỗ trợ: .txt, .pdf, .docx");
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // Lưu file vào uploads
        File uploadedFile = saveUploadedFile(filePart, fileName);
        if (uploadedFile == null) {
            request.setAttribute("error", "Không thể lưu file");
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // Đọc nội dung file
        String content = readFileContent(uploadedFile);
        if (content == null) {
            // Xóa file nếu không đọc được
            uploadedFile.delete();
            request.setAttribute("error", "Không thể đọc nội dung file");
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // AI kiểm duyệt nội dung - QUAN TRỌNG!
        String moderationResult = performContentModeration(content);
        if (moderationResult != null && moderationResult.contains("REJECT")) {
            // Xóa file nếu không hợp lệ
            uploadedFile.delete();
            request.setAttribute("error", "Nội dung không hợp lệ: " + moderationResult);
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // AI trích xuất metadata
        BookMetadata metadata = extractMetadata(content, fileName);
        if (metadata == null) {
            // Xóa file nếu không trích xuất được metadata
            uploadedFile.delete();
            request.setAttribute("error", "Không thể trích xuất thông tin sách");
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
            return;
        }

        // Lưu vào database và lên kệ
        try {
            int createdBookId = saveToDatabase(uploadedFile, metadata, user);
            
            // Redirect về trang chính với thông báo thành công
            String redirectUrl = String.format("%s/?success=ai_upload_completed&bookTitle=%s", 
                request.getContextPath(), java.net.URLEncoder.encode(metadata.title, "UTF-8"));
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            // Xóa file nếu lưu database thất bại
            uploadedFile.delete();
            request.setAttribute("error", "Lỗi lưu database: " + e.getMessage());
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("Content-Disposition header: " + contentDisp);
        
        if (contentDisp != null) {
            String[] tokens = contentDisp.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return token.substring(token.indexOf('=') + 1).trim()
                            .replace("\"", "");
                }
            }
        }
        return null;
    }

    private boolean isSupportedExtension(String fileName) {
        String name = fileName.toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".docx");
    }

    private File saveUploadedFile(Part filePart, String fileName) throws IOException {
        Path uploadPath = Paths.get(UPLOADS_FOLDER, fileName);
        
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ File saved: " + uploadPath.toString());
            return uploadPath.toFile();
        } catch (IOException e) {
            System.err.println("❌ Error saving file: " + e.getMessage());
            return null;
        }
    }

    private String readFileContent(File file) {
        try {
            String ext = getFileExtension(file.getName());
            String content = Utils.readAnyTextFile(file.getAbsolutePath(), ext);
            
            if (content == null || content.trim().isEmpty()) {
                System.err.println("❌ File content is empty");
                return null;
            }
            
            System.out.println("✅ File content read: " + content.length() + " characters");
            return content;
            
        } catch (Exception e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
            return null;
        }
    }

    private String performContentModeration(String content) {
        try {
            System.out.println("🔍 Đang kiểm duyệt nội dung...");
            
            String moderationPrompt = "Hãy kiểm duyệt nội dung sách sau một cách nghiêm ngặt. " +
                "Nếu có nội dung vi phạm (bạo lực, sex, thù ghét, vi phạm pháp luật, spam, v.v.), " +
                "trả về 'REJECT: [lý do cụ thể]'. " +
                "Nếu nội dung an toàn và phù hợp, trả về 'ACCEPT: Nội dung hợp lệ'.\n\n" +
                "Nội dung cần kiểm duyệt: " + content.substring(0, Math.min(2000, content.length()));
            
            String aiResult = aiService.processChat(0, "content-moderation", moderationPrompt, null);
            System.out.println("🤖 Kết quả kiểm duyệt: " + aiResult);
            
            return aiResult;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi kiểm duyệt AI: " + e.getMessage());
            return "REJECT: Lỗi hệ thống kiểm duyệt";
        }
    }

    private boolean checkContentModeration(String content) {
        String result = performContentModeration(content);
        return result != null && result.trim().toUpperCase().contains("ACCEPT");
    }

    private BookMetadata extractMetadata(String content, String fileName) {
        try {
            String metadataPrompt = "Hãy phân tích nội dung sách sau và trả về metadata theo format JSON:\n" +
                "{\n" +
                "  \"title\": \"Tiêu đề sách\",\n" +
                "  \"genre\": \"Thể loại (tiểu thuyết, khoa học viễn tưởng, fantasy, v.v.)\",\n" +
                "  \"description\": \"Mô tả ngắn gọn về sách (2-3 câu)\",\n" +
                "  \"summary\": \"Tóm tắt nội dung chính (5-7 câu)\"\n" +
                "}\n" +
                "Nội dung: " + content.substring(0, Math.min(3000, content.length()));
            
            String aiResult = aiService.processChat(0, "metadata-extraction", metadataPrompt, null);
            System.out.println("🤖 AI Metadata result: " + aiResult);
            
            return parseMetadataFromAI(aiResult, fileName);
            
        } catch (Exception e) {
            System.err.println("❌ AI metadata extraction error: " + e.getMessage());
            return createDefaultMetadata(fileName);
        }
    }

    private BookMetadata parseMetadataFromAI(String aiResult, String fileName) {
        BookMetadata metadata = new BookMetadata();
        
        // Extract title - FIX: Correct offset calculation
        if (aiResult.contains("\"title\":")) {
            int start = aiResult.indexOf("\"title\":") + 8;
            start = aiResult.indexOf("\"", start) + 1; // Skip opening quote
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.title = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract genre - FIX: Correct offset calculation  
        if (aiResult.contains("\"genre\":")) {
            int start = aiResult.indexOf("\"genre\":") + 8;
            start = aiResult.indexOf("\"", start) + 1; // Skip opening quote
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.genre = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract description - FIX: Correct offset calculation
        if (aiResult.contains("\"description\":")) {
            int start = aiResult.indexOf("\"description\":") + 14;
            start = aiResult.indexOf("\"", start) + 1; // Skip opening quote
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.description = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract summary - FIX: Correct offset calculation
        if (aiResult.contains("\"summary\":")) {
            int start = aiResult.indexOf("\"summary\":") + 10;
            start = aiResult.indexOf("\"", start) + 1; // Skip opening quote
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.summary = aiResult.substring(start, end).trim();
            }
        }
        
        // Set defaults if parsing failed
        if (metadata.title == null || metadata.title.isEmpty()) {
            metadata.title = fileName.replaceFirst("[.][^.]+$", ""); // Remove extension
        }
        if (metadata.genre == null || metadata.genre.isEmpty()) {
            metadata.genre = "Tiểu thuyết";
        }
        if (metadata.description == null || metadata.description.isEmpty()) {
            metadata.description = "Sách được upload tự động";
        }
        if (metadata.summary == null || metadata.summary.isEmpty()) {
            metadata.summary = "Nội dung sách đang được xử lý";
        }
        
        return metadata;
    }

    private BookMetadata createDefaultMetadata(String fileName) {
        BookMetadata metadata = new BookMetadata();
        metadata.title = fileName.replaceFirst("[.][^.]+$", "");
        metadata.genre = "Tiểu thuyết";
        metadata.description = "Sách được upload tự động";
        metadata.summary = "Nội dung sách đang được xử lý";
        return metadata;
    }

    private int saveToDatabase(File uploadedFile, BookMetadata metadata, User user) throws SQLException {
        try {
            // Tạo Ebook record
            Ebook ebook = new Ebook();
            ebook.setTitle(metadata.title);
            ebook.setDescription(metadata.description);
            ebook.setReleaseType(metadata.genre);
            ebook.setLanguage("Tiếng Việt");
            ebook.setStatus("active"); // Lên kệ ngay sau khi kiểm duyệt qua
            ebook.setVisibility("public"); // Hiển thị công khai
            ebook.setUploaderId(user.getId());
            ebook.setViewCount(0);
            ebook.setCreatedAt(LocalDateTime.now());

            // Insert Ebook and get generated ID
            ebookDAO.insertEbook(ebook);
            
            // Get the inserted ebook to retrieve the ID
            List<Ebook> books = ebookDAO.selectAllEbooks();
            Ebook insertedBook = books.get(books.size() - 1); // Get the last inserted book
            int ebookId = insertedBook.getId();
            
            System.out.println("✅ Ebook inserted with ID: " + ebookId);

            // Tạo EbookAI record - TỰ ĐỘNG TẠO!
            EbookAI ebookAI = new EbookAI();
            ebookAI.setEbookId(ebookId);
            ebookAI.setFileName(uploadedFile.getName());
            ebookAI.setOriginalFileName(uploadedFile.getName());
            ebookAI.setSummary(metadata.summary);
            ebookAI.setStatus("completed");
            ebookAI.setCreatedAt(LocalDateTime.now());
            ebookAI.setUpdatedAt(LocalDateTime.now());

            // Insert EbookAI
            ebookAIDAO.insertEbookAI(ebookAI);
            
            System.out.println("✅ EbookAI record created successfully");
            System.out.println("🎉 SÁCH ĐÃ ĐƯỢC LÊN KỆ THÀNH CÔNG!");
            System.out.println("📊 Database Summary:");
            System.out.println("   📚 Ebook ID: " + ebookId);
            System.out.println("   📝 Title: " + metadata.title);
            System.out.println("   🏷️ Genre: " + metadata.genre);
            System.out.println("   📁 File: " + uploadedFile.getName());
            System.out.println("   📏 Size: " + formatFileSize(uploadedFile.length()));
            System.out.println("   📍 Path: uploads/" + uploadedFile.getName());
            System.out.println("   🤖 AI Summary: " + metadata.summary.substring(0, Math.min(100, metadata.summary.length())) + "...");
            System.out.println("   📊 AI Status: completed");
            System.out.println("   🌟 Book Status: active (LÊN KỆ)");
            System.out.println("   👁️ Visibility: public");
            
            return ebookId;

        } catch (Exception e) {
            System.err.println("❌ Database error: " + e.getMessage());
            throw new SQLException("Lỗi lưu database: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return (idx > 0) ? fileName.substring(idx + 1).toLowerCase() : "";
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // ============== NEW METHODS FOR SMART BOOK TITLE HANDLING ==============
    
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Lỗi Upload</title>");
        out.println("<style>body{font-family:Arial;margin:40px;background:#ffe6e6;}");
        out.println(".error{background:#f8d7da;color:#721c24;padding:20px;border-radius:8px;border:1px solid #f5c6cb;}</style>");
        out.println("</head><body>");
        out.println("<div class='error'>");
        out.println("<h2>❌ Có lỗi xảy ra</h2>");
        out.println("<p>" + errorMessage + "</p>");
        out.println("<a href='javascript:history.back()' style='color:#007bff;'>← Quay lại</a>");
        out.println("</div></body></html>");
    }

    private void handleSmartFileSearch(HttpServletRequest request, HttpServletResponse response, User user, String bookTitle) 
            throws ServletException, IOException, SQLException {
        
        System.out.println("🔍 Đang tìm file cho sách: " + bookTitle);
        
        // Tìm file phù hợp trong uploads folder
        File matchingFile = findMatchingFile(bookTitle);
        
        if (matchingFile == null) {
            sendErrorResponse(response, "Không tìm thấy file phù hợp với tên sách '" + bookTitle + "' trong thư viện uploads. Vui lòng upload file hoặc kiểm tra lại tên sách.");
            return;
        }
        
        System.out.println("✅ Tìm thấy file phù hợp: " + matchingFile.getName());
        
        // Đọc nội dung file
        String content = readFileContent(matchingFile);
        if (content == null) {
            sendErrorResponse(response, "Không thể đọc nội dung file: " + matchingFile.getName());
            return;
        }

        // AI kiểm duyệt nội dung
        String moderationResult = performContentModeration(content);
        if (moderationResult != null && moderationResult.contains("REJECT")) {
            sendErrorResponse(response, "Nội dung không hợp lệ: " + moderationResult);
            return;
        }

        // AI trích xuất metadata - nhưng sẽ override title
        BookMetadata metadata = extractMetadata(content, matchingFile.getName());
        if (metadata == null) {
            sendErrorResponse(response, "Không thể trích xuất thông tin sách từ file: " + matchingFile.getName());
            return;
        }
        
        // QUAN TRỌNG: Override title với tên người dùng nhập
        metadata.title = bookTitle;
        
        // Lưu vào database
        try {
            int createdBookId = saveToDatabase(matchingFile, metadata, user);
            
            // Redirect về trang chính với thông báo thành công
            String redirectUrl = String.format("%s/?success=smart_upload_completed&bookTitle=%s&originalFile=%s", 
                request.getContextPath(), java.net.URLEncoder.encode(bookTitle, "UTF-8"), matchingFile.getName());
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            sendErrorResponse(response, "Lỗi lưu database: " + e.getMessage());
        }
    }

    private void handleFileUploadWithTitle(HttpServletRequest request, HttpServletResponse response, User user, Part filePart, String bookTitle) 
            throws ServletException, IOException, SQLException {
        
        System.out.println("📚 Đang xử lý upload với title: " + bookTitle);
        
        String fileName = getFileName(filePart);
        if (fileName == null || fileName.trim().isEmpty()) {
            sendErrorResponse(response, "Tên file không hợp lệ");
            return;
        }

        // Kiểm tra định dạng file
        if (!isSupportedExtension(fileName)) {
            sendErrorResponse(response, "Định dạng file không được hỗ trợ. Chỉ hỗ trợ: .txt, .pdf, .docx");
            return;
        }

        // Lưu file vào uploads
        File uploadedFile = saveUploadedFile(filePart, fileName);
        if (uploadedFile == null) {
            sendErrorResponse(response, "Không thể lưu file");
            return;
        }

        // Đọc nội dung file
        String content = readFileContent(uploadedFile);
        if (content == null) {
            uploadedFile.delete();
            sendErrorResponse(response, "Không thể đọc nội dung file");
            return;
        }

        // AI kiểm duyệt nội dung
        String moderationResult = performContentModeration(content);
        if (moderationResult != null && moderationResult.contains("REJECT")) {
            uploadedFile.delete();
            sendErrorResponse(response, "Nội dung không hợp lệ: " + moderationResult);
            return;
        }

        // AI trích xuất metadata
        BookMetadata metadata = extractMetadata(content, fileName);
        if (metadata == null) {
            uploadedFile.delete();
            sendErrorResponse(response, "Không thể trích xuất thông tin sách");
            return;
        }
        
        // QUAN TRỌNG: Override title với tên người dùng nhập
        metadata.title = bookTitle;
        
        // Lưu vào database
        try {
            int createdBookId = saveToDatabase(uploadedFile, metadata, user);
            
            // Redirect về trang chính với thông báo thành công
            String redirectUrl = String.format("%s/?success=title_override_upload_completed&bookTitle=%s", 
                request.getContextPath(), java.net.URLEncoder.encode(bookTitle, "UTF-8"));
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            uploadedFile.delete();
            sendErrorResponse(response, "Lỗi lưu database: " + e.getMessage());
        }
    }

    private File findMatchingFile(String bookTitle) {
        File uploadsDir = new File(UPLOADS_FOLDER);
        if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
            System.err.println("❌ Uploads directory not found: " + UPLOADS_FOLDER);
            return null;
        }
        
        File[] files = uploadsDir.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".txt") || lowerName.endsWith(".pdf") || lowerName.endsWith(".docx");
        });
        
        if (files == null || files.length == 0) {
            System.out.println("📁 Không có file nào trong uploads folder");
            return null;
        }
        
        String normalizedTitle = normalizeTitle(bookTitle);
        File bestMatch = null;
        double bestSimilarity = 0.0;
        
        System.out.println("🔍 Tìm kiếm file cho: '" + normalizedTitle + "'");
        System.out.println("📁 Kiểm tra " + files.length + " files trong uploads:");
        
        for (File file : files) {
            String fileName = file.getName();
            // Remove extension for comparison
            String fileNameWithoutExt = fileName.replaceFirst("[.][^.]+$", "");
            String normalizedFileName = normalizeTitle(fileNameWithoutExt);
            
            // Calculate similarity
            double similarity = calculateStringSimilarity(normalizedTitle, normalizedFileName);
            
            System.out.println("   📄 " + fileName + " → Similarity: " + String.format("%.2f", similarity));
            
            // Exact match or very high similarity
            if (similarity > 0.8 || normalizedFileName.contains(normalizedTitle) || normalizedTitle.contains(normalizedFileName)) {
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity;
                    bestMatch = file;
                }
            }
        }
        
        if (bestMatch != null) {
            System.out.println("✅ Best match found: " + bestMatch.getName() + " (similarity: " + String.format("%.2f", bestSimilarity) + ")");
        } else {
            System.out.println("❌ No suitable match found for: " + bookTitle);
        }
        
        return bestMatch;
    }
    
    private String normalizeTitle(String title) {
        return title.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
    
    private double calculateStringSimilarity(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;
        
        int longer = Math.max(s1.length(), s2.length());
        if (longer == 0) return 1.0;
        
        return (longer - levenshteinDistance(s1, s2)) / (double) longer;
    }
    
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i-1) == s2.charAt(j-1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1), dp[i-1][j-1] + cost);
            }
        }
        
        return dp[s1.length()][s2.length()];
    }

    // Inner class để hold metadata
    static class BookMetadata {
        String title;
        String genre;
        String description;
        String summary;
    }
} 