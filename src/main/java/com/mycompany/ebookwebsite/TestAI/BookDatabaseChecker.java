package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 🔍 Book Database Checker
 * Kiểm tra sách đã được lưu vào database và listBook
 */
public class BookDatabaseChecker {
    
    public static void main(String[] args) {
        System.out.println("🔍 BOOK DATABASE CHECKER");
        System.out.println("========================");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n📋 MENU KIỂM TRA:");
            System.out.println("1. 🔍 Tìm sách theo tên");
            System.out.println("2. 📚 Xem tất cả sách trong database");
            System.out.println("3. 📁 Xem sách trong listBook");
            System.out.println("4. 🔄 Kiểm tra sách mới upload");
            System.out.println("5. 🔗 Test kết nối database");
            System.out.println("6. 📊 Thống kê database");
            System.out.println("0. 🚪 Thoát");
            
            System.out.print("\nChọn tùy chọn (0-6): ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    searchBookByName(scanner);
                    break;
                case "2":
                    showAllBooksInDatabase();
                    break;
                case "3":
                    showBooksInListBook();
                    break;
                case "4":
                    checkRecentlyUploadedBooks();
                    break;
                case "5":
                    testDatabaseConnection();
                    break;
                case "6":
                    showDatabaseStats();
                    break;
                case "0":
                    System.out.println("👋 Tạm biệt!");
                    return;
                default:
                    System.out.println("⚠️ Lựa chọn không hợp lệ!");
            }
        }
    }
    
    private static void testDatabaseConnection() {
        System.out.println("\n🔗 TEST KẾT NỐI DATABASE:");
        System.out.println("=".repeat(50));
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Kết nối database thành công!");
                System.out.println("📊 Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("🔢 Version: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("📁 Database Name: " + conn.getCatalog());
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối database: " + e.getMessage());
        }
    }
    
    private static void showDatabaseStats() {
        System.out.println("\n📊 THỐNG KÊ DATABASE:");
        System.out.println("=".repeat(50));
        
        try (Connection conn = DBConnection.getConnection()) {
            // Đếm số sách
            String countSql = "SELECT COUNT(*) as total_books FROM Ebooks WHERE status != 'deleted'";
            PreparedStatement countStmt = conn.prepareStatement(countSql);
            ResultSet countRs = countStmt.executeQuery();
            
            if (countRs.next()) {
                System.out.println("📚 Tổng số sách: " + countRs.getInt("total_books"));
            }
            
            // Thống kê theo trạng thái
            String statusSql = "SELECT status, COUNT(*) as count FROM Ebooks GROUP BY status";
            PreparedStatement statusStmt = conn.prepareStatement(statusSql);
            ResultSet statusRs = statusStmt.executeQuery();
            
            System.out.println("\n📈 Thống kê theo trạng thái:");
            while (statusRs.next()) {
                System.out.println("   " + statusRs.getString("status") + ": " + statusRs.getInt("count"));
            }
            
            // Sách mới nhất
            String recentSql = "SELECT TOP 5 title, created_at FROM Ebooks ORDER BY created_at DESC";
            PreparedStatement recentStmt = conn.prepareStatement(recentSql);
            ResultSet recentRs = recentStmt.executeQuery();
            
            System.out.println("\n🆕 Sách mới nhất:");
            while (recentRs.next()) {
                System.out.println("   📚 " + recentRs.getString("title"));
                System.out.println("   📅 " + recentRs.getString("created_at"));
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thống kê: " + e.getMessage());
        }
    }
    
    private static void searchBookByName(Scanner scanner) {
        System.out.print("\n🔍 Nhập tên sách cần tìm: ");
        String bookName = scanner.nextLine().trim();
        
        System.out.println("\n📚 KẾT QUẢ TÌM KIẾM:");
        System.out.println("=".repeat(50));
        
        // Tìm trong database
        List<BookInfo> dbBooks = searchInDatabase(bookName);
        if (!dbBooks.isEmpty()) {
            System.out.println("✅ Tìm thấy trong DATABASE:");
            for (BookInfo book : dbBooks) {
                System.out.println("   📚 " + book.title);
                System.out.println("   🏷️ " + book.genre);
                System.out.println("   📁 " + book.fileName);
                System.out.println("   📅 " + book.uploadDate);
                System.out.println("   📊 Status: " + book.status);
                System.out.println("   ---");
            }
        } else {
            System.out.println("❌ Không tìm thấy trong database");
        }
        
        // Tìm trong listBook
        List<BookInfo> listBooks = searchInListBook(bookName);
        if (!listBooks.isEmpty()) {
            System.out.println("✅ Tìm thấy trong LISTBOOK:");
            for (BookInfo book : listBooks) {
                System.out.println("   📚 " + book.title);
                System.out.println("   🏷️ " + book.genre);
                System.out.println("   📁 " + book.fileName);
                System.out.println("   ---");
            }
        } else {
            System.out.println("❌ Không tìm thấy trong listBook");
        }
    }
    
    private static void showAllBooksInDatabase() {
        System.out.println("\n📚 TẤT CẢ SÁCH TRONG DATABASE:");
        System.out.println("=".repeat(50));
        
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra bảng Ebooks có tồn tại không
            String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Ebooks'";
            PreparedStatement checkStmt = conn.prepareStatement(checkTableSql);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                String sql = "SELECT id, title, description, release_type, status, visibility, created_at, view_count FROM Ebooks WHERE status != 'deleted' ORDER BY created_at DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(count + ". 📚 " + rs.getString("title"));
                    System.out.println("   🏷️ Thể loại: " + rs.getString("release_type"));
                    System.out.println("   📅 Upload: " + rs.getString("created_at"));
                    System.out.println("   📊 Status: " + rs.getString("status"));
                    System.out.println("   👁️ Views: " + rs.getInt("view_count"));
                    System.out.println("   ---");
                }
                
                if (count == 0) {
                    System.out.println("📭 Không có sách nào trong database");
                } else {
                    System.out.println("📊 Tổng cộng: " + count + " sách");
                }
            } else {
                System.out.println("❌ Bảng 'Ebooks' chưa được tạo trong database");
                System.out.println("💡 Cần tạo bảng Ebooks trước khi sử dụng");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Lỗi database: " + e.getMessage());
        }
    }
    
    private static void showBooksInListBook() {
        System.out.println("\n📚 SÁCH TRONG LISTBOOK:");
        System.out.println("=".repeat(50));
        
        try {
            // Giả lập listBook (trong thực tế sẽ đọc từ file hoặc service)
            List<BookInfo> listBooks = getListBookBooks();
            
            if (listBooks.isEmpty()) {
                System.out.println("📭 Không có sách nào trong listBook");
            } else {
                for (int i = 0; i < listBooks.size(); i++) {
                    BookInfo book = listBooks.get(i);
                    System.out.println((i + 1) + ". 📚 " + book.title);
                    System.out.println("   🏷️ " + book.genre);
                    System.out.println("   📁 " + book.fileName);
                    System.out.println("   ---");
                }
                System.out.println("📊 Tổng cộng: " + listBooks.size() + " sách");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi đọc listBook: " + e.getMessage());
        }
    }
    
    private static void checkRecentlyUploadedBooks() {
        System.out.println("\n🔄 KIỂM TRA SÁCH MỚI UPLOAD:");
        System.out.println("=".repeat(50));
        
        // Kiểm tra file trong uploads
        System.out.println("📁 Files trong uploads/:");
        java.io.File uploadsDir = new java.io.File("D:\\EbookWebsite\\uploads");
        if (uploadsDir.exists() && uploadsDir.isDirectory()) {
            java.io.File[] files = uploadsDir.listFiles();
            if (files != null && files.length > 0) {
                for (java.io.File file : files) {
                    if (file.isFile()) {
                        System.out.println("   📄 " + file.getName() + " (" + formatFileSize(file.length()) + ")");
                    }
                }
            } else {
                System.out.println("   📭 Không có file nào");
            }
        } else {
            System.out.println("   ❌ Thư mục uploads không tồn tại");
        }
        
        // Kiểm tra trong database
        System.out.println("\n📚 Sách mới trong database:");
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra bảng Ebooks có tồn tại không
            String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Ebooks'";
            PreparedStatement checkStmt = conn.prepareStatement(checkTableSql);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                String sql = "SELECT TOP 10 title, created_at, status FROM Ebooks WHERE created_at >= DATEADD(day, -1, GETDATE()) ORDER BY created_at DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("   📚 " + rs.getString("title"));
                    System.out.println("   📅 " + rs.getString("created_at"));
                    System.out.println("   📊 " + rs.getString("status"));
                }
                
                if (count == 0) {
                    System.out.println("   📭 Không có sách mới");
                }
            } else {
                System.out.println("   ❌ Bảng 'Ebooks' chưa được tạo");
            }
            
        } catch (SQLException e) {
            System.out.println("   ❌ Lỗi database: " + e.getMessage());
        }
    }
    
    private static List<BookInfo> searchInDatabase(String bookName) {
        List<BookInfo> books = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra bảng Ebooks có tồn tại không
            String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Ebooks'";
            PreparedStatement checkStmt = conn.prepareStatement(checkTableSql);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                String sql = "SELECT id, title, release_type, created_at, status FROM Ebooks WHERE title LIKE ? AND status != 'deleted'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                String searchPattern = "%" + bookName + "%";
                stmt.setString(1, searchPattern);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    BookInfo book = new BookInfo();
                    book.title = rs.getString("title");
                    book.genre = rs.getString("release_type");
                    book.fileName = "N/A"; // File info moved to EbookAI table
                    book.originalFileName = "N/A"; // File info moved to EbookAI table
                    book.uploadDate = rs.getString("created_at");
                    book.status = rs.getString("status");
                    books.add(book);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm database: " + e.getMessage());
        }
        
        return books;
    }
    
    private static List<BookInfo> searchInListBook(String bookName) {
        List<BookInfo> books = new ArrayList<>();
        
        try {
            List<BookInfo> allBooks = getListBookBooks();
            for (BookInfo book : allBooks) {
                if (book.title.toLowerCase().contains(bookName.toLowerCase()) ||
                    book.fileName.toLowerCase().contains(bookName.toLowerCase())) {
                    books.add(book);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi tìm kiếm listBook: " + e.getMessage());
        }
        
        return books;
    }
    
    private static List<BookInfo> getListBookBooks() {
        List<BookInfo> books = new ArrayList<>();
        
        // Giả lập dữ liệu listBook (trong thực tế sẽ đọc từ file hoặc service)
        BookInfo book1 = new BookInfo();
        book1.title = "Chú Bé Thành Paris - Victor Hugo";
        book1.genre = "Tiểu thuyết";
        book1.fileName = "Chú Bé Thành Paris - Victor Hugo.pdf";
        books.add(book1);
        
        return books;
    }
    
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    static class BookInfo {
        String title;
        String genre;
        String fileName;
        String originalFileName;
        String uploadDate;
        String status;
    }
} 