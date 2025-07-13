package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.utils.PathManager;
import java.io.File;

/**
 * 🗂️ PathManager Test - Kiểm tra đường dẫn project thông minh
 * 
 * Test để kiểm tra PathManager có hoạt động đúng không
 * và fix các vấn đề đường dẫn hard-coded
 */
public class PathManagerTest {
    
    public static void main(String[] args) {
        System.out.println("🗂️ PATHMANAGER TEST");
        System.out.println("==================");
        
        // Test 1: Kiểm tra project root detection
        System.out.println("\n1. 🔍 Project Root Detection:");
        String projectRoot = PathManager.getProjectRoot();
        System.out.println("Project Root: " + projectRoot);
        
        // Test 2: Kiểm tra uploads path
        System.out.println("\n2. 📁 Uploads Path:");
        String uploadsPath = PathManager.getUploadsPath();
        System.out.println("Uploads Path: " + uploadsPath);
        
        // Test 3: Kiểm tra uploads directory tồn tại
        System.out.println("\n3. ✅ Directory Check:");
        File uploadsDir = new File(uploadsPath);
        System.out.println("Uploads exists: " + uploadsDir.exists());
        System.out.println("Is directory: " + uploadsDir.isDirectory());
        
        if (uploadsDir.exists() && uploadsDir.isDirectory()) {
            File[] files = uploadsDir.listFiles();
            System.out.println("Files count: " + (files != null ? files.length : 0));
            
            if (files != null && files.length > 0) {
                System.out.println("\n📄 Files in uploads:");
                for (File file : files) {
                    System.out.println("  - " + file.getName() + " (" + formatFileSize(file.length()) + ")");
                }
            }
        }
        
        // Test 4: Test file search functionality
        System.out.println("\n4. 🔍 File Search Test:");
        System.out.println("Searching for 'Nhà Thờ Đức Bà Paris'...");
        File foundFile = PathManager.findSimilarFile("Nhà Thờ Đức Bà Paris");
        if (foundFile != null) {
            System.out.println("✅ Found: " + foundFile.getName());
        } else {
            System.out.println("❌ No similar file found");
        }
        
        // Test 5: Test với file path creation
        System.out.println("\n5. 🔧 File Path Creation:");
        String testFileName = "test-book.txt";
        String filePath = PathManager.getUploadFilePath(testFileName);
        System.out.println("File path for '" + testFileName + "': " + filePath);
        
        // Test 6: Debug info
        System.out.println("\n6. 📊 Debug Info:");
        System.out.println(PathManager.getDebugInfo());
        
        // Test 7: Kiểm tra current directory để debug
        System.out.println("\n7. 🔍 Current Directory Debug:");
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println("catalina.base: " + System.getProperty("catalina.base"));
        
        // Test 8: Kiểm tra environment variables
        System.out.println("\n8. 🌍 Environment Variables:");
        String uploadFolderEnv = System.getenv("UPLOAD_FOLDER");
        System.out.println("UPLOAD_FOLDER env: " + uploadFolderEnv);
        String uploadFolderProp = System.getProperty("UPLOAD_FOLDER");
        System.out.println("UPLOAD_FOLDER prop: " + uploadFolderProp);
        
        System.out.println("\n✅ PathManager Test Completed!");
        System.out.println("==================================");
        
        // Recommendations
        System.out.println("\n💡 Recommendations:");
        System.out.println("1. Để set custom upload folder:");
        System.out.println("   - Tạo file .env với UPLOAD_FOLDER=your_path");
        System.out.println("   - Hoặc set environment variable: UPLOAD_FOLDER=your_path");
        System.out.println("   - Hoặc JVM arg: -DUPLOAD_FOLDER=your_path");
        
        System.out.println("2. Upload path hiện tại: " + uploadsPath);
        System.out.println("3. Nếu muốn thay đổi, restart application sau khi set env var");
    }
    
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
} 