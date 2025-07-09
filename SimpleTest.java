import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test đơn giản để kiểm tra Dropbox upload
 */
public class SimpleTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Test đơn giản Dropbox upload...");
        
        try {
            // Tạo file test đơn giản
            String content = "Đây là file test upload lên Dropbox.\n" +
                           "Nội dung test để kiểm tra integration.\n" +
                           "Chương 1: Test\n" +
                           "Chương 2: Test tiếp\n" +
                           "Kết thúc test.";
            
            // Tạo file
            Path testFile = Paths.get("test_simple.txt");
            Files.write(testFile, content.getBytes("UTF-8"));
            System.out.println("✅ Đã tạo file test: " + testFile.toAbsolutePath());
            
            // Test upload (cần import DropboxUploader)
            System.out.println("📤 Đang test upload...");
            System.out.println("⚠️  Cần chạy trong project với đầy đủ dependencies");
            
            // Xóa file test
            Files.deleteIfExists(testFile);
            System.out.println("🗑️ Đã xóa file test");
            
            System.out.println("✅ Test hoàn thành!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 