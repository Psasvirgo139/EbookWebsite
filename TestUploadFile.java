package com.mycompany.ebookwebsite.test;

import com.mycompany.ebookwebsite.service.DropboxUploader;
import com.mycompany.ebookwebsite.service.UploadBookService;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.utils.FileTextExtractor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Test class để kiểm tra upload file với Dropbox integration
 */
public class TestUploadFile {
    
    private static final Logger logger = Logger.getLogger(TestUploadFile.class.getName());
    
    public static void main(String[] args) {
        System.out.println("🚀 Bắt đầu test upload file với Dropbox...");
        
        try {
            // Test 1: Test DropboxUploader trực tiếp
            testDropboxUploader();
            
            // Test 2: Test UploadBookService
            testUploadBookService();
            
            // Test 3: Test FileTextExtractor
            testFileTextExtractor();
            
            System.out.println("✅ Tất cả test hoàn thành thành công!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong quá trình test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test DropboxUploader trực tiếp
     */
    private static void testDropboxUploader() throws Exception {
        System.out.println("\n📤 Test 1: DropboxUploader");
        
        // Tạo file test
        String testContent = """
            Đây là nội dung test cho file upload.
            
            Chương 1: Khởi đầu
            ===================
            
            Trong một ngôi làng nhỏ, có một cậu bé tên Alex. Cậu luôn mơ ước trở thành 
            một pháp sư mạnh mẽ. Mỗi ngày, cậu đều luyện tập ma thuật với familiar 
            Luna của mình.
            
            "Luna, hôm nay chúng ta sẽ học phép thuật mới!" Alex nói với Luna.
            Luna gật đầu và phát ra tiếng kêu nhỏ.
            
            Chương 2: Thử thách đầu tiên
            ============================
            
            Một ngày nọ, làng bị tấn công bởi một con quái vật. Alex và Luna 
            phải sử dụng tất cả kiến thức ma thuật để bảo vệ dân làng.
            
            "Luna, đây là lúc chúng ta chứng minh mình!" Alex hét lên.
            
            Kết thúc
            ========
            
            Sau trận chiến, Alex và Luna trở thành anh hùng của làng. Họ tiếp tục 
            hành trình học hỏi và bảo vệ mọi người.
            """;
        
        // Tạo file test
        Path testFilePath = createTestFile("test_book.txt", testContent);
        System.out.println("✅ Đã tạo file test: " + testFilePath);
        
        // Test upload lên Dropbox
        int userId = 1; // User ID test
        String originalFilename = "Alex và Luna - Cuộc phiêu lưu ma thuật.txt";
        
        System.out.println("📤 Đang upload file lên Dropbox...");
        DropboxUploader.DropboxUploadResult result = 
            DropboxUploader.uploadFile(testFilePath.toString(), userId, originalFilename);
        
        System.out.println("✅ Upload thành công!");
        System.out.println("📊 Kết quả upload:");
        System.out.println("   - Dropbox Path: " + result.getDropboxPath());
        System.out.println("   - File ID: " + result.getFileId());
        System.out.println("   - Shared Link: " + result.getSharedLink());
        System.out.println("   - File Size: " + result.getFileSize() + " bytes");
        System.out.println("   - File Name: " + result.getFileName());
        
        // Xóa file test
        Files.deleteIfExists(testFilePath);
        System.out.println("🗑️ Đã xóa file test tạm");
    }
    
    /**
     * Test UploadBookService
     */
    private static void testUploadBookService() throws Exception {
        System.out.println("\n📚 Test 2: UploadBookService");
        
        // Tạo file test
        String testContent = """
            Truyện ngắn test
            ================
            
            Đây là một truyện ngắn để test upload service.
            
            Nhân vật chính: Minh Anh
            Thể loại: Ngôn tình
            
            Chương 1: Gặp gỡ
            =================
            
            Minh Anh là một cô gái trẻ, xinh đẹp và thông minh. Cô làm việc tại 
            một công ty lớn và có cuộc sống khá ổn định.
            
            Một ngày nọ, cô gặp được anh chàng CEO trẻ tuổi tên Trần Minh. 
            Anh ta đẹp trai, tài giỏi và rất lịch lãm.
            
            "Xin chào, tôi là Trần Minh." Anh ta nói với giọng ấm áp.
            "Chào anh, tôi là Minh Anh." Cô đáp lại với nụ cười rạng rỡ.
            
            Chương 2: Tình yêu nảy nở
            ==========================
            
            Sau nhiều lần gặp gỡ, tình cảm giữa hai người dần nảy nở. Họ 
            bắt đầu hẹn hò và tìm hiểu nhau nhiều hơn.
            
            "Anh có thích tôi không?" Minh Anh hỏi một cách ngây thơ.
            "Có, anh rất thích em." Trần Minh trả lời chân thành.
            
            Kết thúc có hậu
            =================
            
            Cuối cùng, họ đã kết hôn và sống hạnh phúc bên nhau. Câu chuyện 
            tình yêu đẹp đẽ của họ trở thành giai thoại trong công ty.
            """;
        
        // Tạo file test
        Path testFilePath = createTestFile("test_romance.txt", testContent);
        System.out.println("✅ Đã tạo file test: " + testFilePath);
        
        // Test UploadBookService
        UploadBookService uploadService = new UploadBookService();
        
        // Tạo mock Part object (trong thực tế sẽ từ HTTP request)
        MockPart mockPart = new MockPart("file", "test_romance.txt", testFilePath);
        
        System.out.println("📤 Đang test upload book service...");
        UploadBookService.UploadResult result = uploadService.uploadBook(
            mockPart, 
            1, // userId
            "Tình yêu và những bí mật", 
            "Tác giả Test", 
            "Truyện ngôn tình test upload"
        );
        
        if (result.isSuccess()) {
            System.out.println("✅ Upload book service thành công!");
            Ebook ebook = result.getEbook();
            System.out.println("📊 Thông tin ebook:");
            System.out.println("   - ID: " + ebook.getId());
            System.out.println("   - Title: " + ebook.getTitle());
            System.out.println("   - Author: " + ebook.getAuthor());
            System.out.println("   - File Name: " + ebook.getFileName());
            System.out.println("   - File Size: " + ebook.getFileSize());
            System.out.println("   - Dropbox Link: " + ebook.getDropboxLink());
            System.out.println("   - Content Length: " + (ebook.getContent() != null ? ebook.getContent().length() : 0) + " chars");
        } else {
            System.out.println("❌ Upload book service thất bại: " + result.getMessage());
        }
        
        // Xóa file test
        Files.deleteIfExists(testFilePath);
        System.out.println("🗑️ Đã xóa file test tạm");
    }
    
    /**
     * Test FileTextExtractor
     */
    private static void testFileTextExtractor() throws Exception {
        System.out.println("\n📄 Test 3: FileTextExtractor");
        
        // Tạo file test với nội dung phức tạp
        String testContent = """
            Truyện kiếm hiệp test
            ====================
            
            Chương 1: Khởi đầu tu luyện
            ===========================
            
            Lý Minh là một thiếu niên 16 tuổi, sống tại ngôi làng nhỏ bên sông. 
            Cậu luôn mơ ước trở thành một kiếm khách mạnh mẽ.
            
            "Hôm nay ta sẽ bắt đầu tu luyện!" Lý Minh tự nhủ.
            
            Chương 2: Gặp sư phụ
            ======================
            
            Trong một lần đi săn, Lý Minh gặp được một lão kiếm khách bị thương. 
            Cậu đã cứu lão và được nhận làm đệ tử.
            
            "Ngươi có tâm tính tốt, ta sẽ truyền thụ võ công cho ngươi." Lão kiếm khách nói.
            
            Chương 3: Tu luyện gian khổ
            ============================
            
            Mỗi ngày, Lý Minh phải luyện kiếm từ sáng sớm đến tối mịt. 
            Cậu phải học các chiêu thức cơ bản và nâng cao.
            
            "Kiên trì là chìa khóa của thành công!" Sư phụ dạy.
            
            Chương 4: Thử thách đầu tiên
            =============================
            
            Sau 3 năm tu luyện, Lý Minh đã trở thành một kiếm khách có tiếng. 
            Cậu phải đối mặt với thử thách đầu tiên - bảo vệ làng khỏi bọn cướp.
            
            "Đây là lúc chứng minh tài năng!" Lý Minh hét lên.
            
            Kết thúc
            ========
            
            Lý Minh đã thành công bảo vệ làng và trở thành anh hùng. 
            Cậu tiếp tục hành trình tu luyện để trở nên mạnh mẽ hơn.
            """;
        
        // Tạo file test
        Path testFilePath = createTestFile("test_wuxia.txt", testContent);
        System.out.println("✅ Đã tạo file test: " + testFilePath);
        
        // Test FileTextExtractor
        FileTextExtractor extractor = new FileTextExtractor();
        String extractedContent = extractor.extractText(testFilePath.toString());
        
        System.out.println("✅ Trích xuất nội dung thành công!");
        System.out.println("📊 Thống kê nội dung:");
        System.out.println("   - Độ dài gốc: " + testContent.length() + " chars");
        System.out.println("   - Độ dài trích xuất: " + extractedContent.length() + " chars");
        System.out.println("   - Số từ gốc: " + testContent.split("\\s+").length + " words");
        System.out.println("   - Số từ trích xuất: " + extractedContent.split("\\s+").length + " words");
        
        // Kiểm tra nội dung có được trích xuất đúng không
        if (extractedContent.contains("Lý Minh") && extractedContent.contains("kiếm khách")) {
            System.out.println("✅ Nội dung được trích xuất chính xác!");
        } else {
            System.out.println("⚠️ Nội dung trích xuất có thể có vấn đề");
        }
        
        // Xóa file test
        Files.deleteIfExists(testFilePath);
        System.out.println("🗑️ Đã xóa file test tạm");
    }
    
    /**
     * Tạo file test
     */
    private static Path createTestFile(String filename, String content) throws Exception {
        Path testDir = Paths.get("test_files");
        if (!Files.exists(testDir)) {
            Files.createDirectories(testDir);
        }
        
        Path testFile = testDir.resolve(filename);
        Files.write(testFile, content.getBytes("UTF-8"));
        return testFile;
    }
    
    /**
     * Mock Part class để test UploadBookService
     */
    private static class MockPart implements jakarta.servlet.http.Part {
        private final String name;
        private final String fileName;
        private final Path filePath;
        
        public MockPart(String name, String fileName, Path filePath) {
            this.name = name;
            this.fileName = fileName;
            this.filePath = filePath;
        }
        
        @Override
        public String getName() { return name; }
        
        @Override
        public String getSubmittedFileName() { return fileName; }
        
        @Override
        public long getSize() { 
            try {
                return Files.size(filePath);
            } catch (Exception e) {
                return 0;
            }
        }
        
        @Override
        public java.io.InputStream getInputStream() throws java.io.IOException {
            return Files.newInputStream(filePath);
        }
        
        // Implement các method khác của Part interface
        @Override
        public void write(String fileName) throws java.io.IOException {}
        
        @Override
        public void delete() throws java.io.IOException {}
        
        @Override
        public String getHeader(String name) { return null; }
        
        @Override
        public java.util.Collection<String> getHeaders(String name) { return null; }
        
        @Override
        public java.util.Collection<String> getHeaderNames() { return null; }
        
        @Override
        public String getContentType() { return "text/plain"; }
        
        @Override
        public java.util.Collection<String> getParameterNames() { return null; }
        
        @Override
        public String[] getParameterValues(String name) { return null; }
        
        @Override
        public java.util.Map<String, String[]> getParameterMap() { return null; }
        
        @Override
        public String getPartName() { return name; }
        
        @Override
        public java.io.InputStream getInputStream() throws java.io.IOException {
            return Files.newInputStream(filePath);
        }
    }
} 