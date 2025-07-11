package com.mycompany.ebookwebsite.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * 🗂️ PathManager - Quản lý đường dẫn project thông minh
 * 
 * Tự động phát hiện đường dẫn project và tạo các paths phù hợp
 * Thay thế hard-coded paths để linh hoạt khi deploy
 */
public class PathManager {
    
    private static final Logger logger = Logger.getLogger(PathManager.class.getName());
    
    private static String projectRoot = null;
    private static String uploadsPath = null;
    private static boolean initialized = false;
    
    static {
        initialize();
    }
    
    /**
     * 🔍 Khởi tạo và tìm project root thông minh
     */
    private static void initialize() {
        if (initialized) return;
        
        try {
            // 1. Thử từ environment variable trước
            String envUploadFolder = Utils.getEnv("UPLOAD_FOLDER");
            if (envUploadFolder != null && !envUploadFolder.isEmpty()) {
                File envDir = new File(envUploadFolder);
                if (envDir.exists() && envDir.isDirectory()) {
                    uploadsPath = envUploadFolder;
                    projectRoot = envDir.getParentFile().getAbsolutePath();
                    logger.info("✅ Using upload path from environment: " + uploadsPath);
                    initialized = true;
                    return;
                }
            }
            
            // 2. Tìm EbookWebsite project trực tiếp (ưu tiên cao)
            String currentDir = System.getProperty("user.dir");
            logger.info("🔍 Current directory: " + currentDir);
            
            projectRoot = findEbookWebsiteProject();
            if (projectRoot != null) {
                uploadsPath = projectRoot + File.separator + "uploads";
                createUploadsDirectory();
                logger.info("✅ EbookWebsite project found: " + projectRoot);
                logger.info("✅ Uploads path: " + uploadsPath);
                initialized = true;
                return;
            }
            
            // 3. Fallback: Auto-detect từ current directory
            projectRoot = detectProjectRoot(currentDir);
            if (projectRoot != null) {
                uploadsPath = projectRoot + File.separator + "uploads";
                createUploadsDirectory();
                logger.info("✅ Project root detected from current dir: " + projectRoot);
                logger.info("✅ Uploads path: " + uploadsPath);
            } else {
                // 4. Emergency fallback
                projectRoot = currentDir;
                uploadsPath = projectRoot + File.separator + "uploads";
                createUploadsDirectory();
                logger.warning("⚠️ Using emergency fallback project root: " + projectRoot);
            }
            
            initialized = true;
            
        } catch (Exception e) {
            logger.severe("❌ Error initializing PathManager: " + e.getMessage());
            // Emergency fallback
            projectRoot = System.getProperty("user.dir");
            uploadsPath = projectRoot + File.separator + "uploads";
            initialized = true;
        }
    }
    
    /**
     * 🎯 Tìm EbookWebsite project trực tiếp (ưu tiên cao)
     */
    private static String findEbookWebsiteProject() {
        // Các đường dẫn có thể có của EbookWebsite project
        String[] possiblePaths = {
            "D:\\Code\\dev files\\Java\\Netbeans\\EbookWebsite",  // Đường dẫn đúng
            "D:\\EbookWebsite",  // Old path
            "C:\\Code\\dev files\\Java\\Netbeans\\EbookWebsite",  // Alternative drive
            System.getProperty("user.home") + "\\EbookWebsite",  // User home
            System.getProperty("user.home") + "\\Code\\dev files\\Java\\Netbeans\\EbookWebsite"  // User home variant
        };
        
        logger.info("🔍 Searching for EbookWebsite project in known locations...");
        
        for (String path : possiblePaths) {
            File projectDir = new File(path);
            logger.info("📁 Checking: " + path);
            
            if (isEbookWebsiteProject(projectDir)) {
                logger.info("🎯 Found EbookWebsite project at: " + path);
                return path;
            }
        }
        
        // Tìm trong drives có thể có
        String[] drives = {"C:", "D:", "E:", "F:"};
        for (String drive : drives) {
            String searchPath = drive + "\\Code\\dev files\\Java\\Netbeans\\EbookWebsite";
            File projectDir = new File(searchPath);
            logger.info("📁 Checking drive: " + searchPath);
            
            if (isEbookWebsiteProject(projectDir)) {
                logger.info("🎯 Found EbookWebsite project at: " + searchPath);
                return searchPath;
            }
        }
        
        logger.warning("⚠️ EbookWebsite project not found in known locations");
        return null;
    }
    
    /**
     * 📋 Kiểm tra xem directory có phải EbookWebsite project không
     */
    private static boolean isEbookWebsiteProject(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        
        // Kiểm tra các marker files của EbookWebsite
        File pomFile = new File(dir, "pom.xml");
        File srcDir = new File(dir, "src");
        File mainDir = new File(dir, "src/main/java/com/mycompany/ebookwebsite");
        
        boolean hasMarkers = pomFile.exists() && srcDir.exists() && mainDir.exists();
        
        if (hasMarkers) {
            logger.info("✅ Valid EbookWebsite project markers found in: " + dir.getAbsolutePath());
        }
        
        return hasMarkers;
    }

    /**
     * 🕵️ Phát hiện project root bằng cách tìm marker files
     */
    private static String detectProjectRoot(String startPath) {
        File current = new File(startPath);
        
        // Marker files/directories để identify project root
        String[] markers = {
            "pom.xml",           // Maven project
            "build.gradle",      // Gradle project  
            "src",              // Source directory
            ".git",             // Git repository
            "nb-configuration.xml", // NetBeans project
            "target"            // Maven build directory
        };
        
        // Tìm lên tối đa 5 level
        for (int i = 0; i < 5 && current != null; i++) {
            // Kiểm tra marker files
            for (String marker : markers) {
                File markerFile = new File(current, marker);
                if (markerFile.exists()) {
                    logger.info("🎯 Found project marker: " + marker + " in " + current.getAbsolutePath());
                    return current.getAbsolutePath();
                }
            }
            
            // Kiểm tra nếu là NetBeans EbookWebsite project
            if (current.getName().equals("EbookWebsite")) {
                File srcDir = new File(current, "src");
                File pomFile = new File(current, "pom.xml");
                if (srcDir.exists() && pomFile.exists()) {
                    logger.info("🎯 Found EbookWebsite project: " + current.getAbsolutePath());
                    return current.getAbsolutePath();
                }
            }
            
            current = current.getParentFile();
        }
        
        return null;
    }
    
    /**
     * 📁 Tạo uploads directory nếu chưa có
     */
    private static void createUploadsDirectory() {
        if (uploadsPath != null) {
            File uploadsDir = new File(uploadsPath);
            if (!uploadsDir.exists()) {
                boolean created = uploadsDir.mkdirs();
                if (created) {
                    logger.info("📁 Created uploads directory: " + uploadsPath);
                } else {
                    logger.warning("⚠️ Failed to create uploads directory: " + uploadsPath);
                }
            }
        }
    }
    
    /**
     * 🗂️ Lấy đường dẫn project root
     */
    public static String getProjectRoot() {
        initialize();
        return projectRoot;
    }
    
    /**
     * 📁 Lấy đường dẫn uploads folder
     */
    public static String getUploadsPath() {
        initialize();
        return uploadsPath;
    }
    
    /**
     * 📄 Tạo đường dẫn file trong uploads
     */
    public static String getUploadFilePath(String fileName) {
        initialize();
        if (fileName == null || fileName.trim().isEmpty()) {
            return uploadsPath;
        }
        return uploadsPath + File.separator + fileName;
    }
    
    /**
     * 🔧 Lấy Path object cho uploads
     */
    public static Path getUploadsPathObj() {
        initialize();
        return Paths.get(uploadsPath);
    }
    
    /**
     * 🔧 Lấy Path object cho file trong uploads
     */
    public static Path getUploadFilePathObj(String fileName) {
        initialize();
        return Paths.get(uploadsPath, fileName);
    }
    
    /**
     * 📂 Kiểm tra và tạo subdirectory trong uploads
     */
    public static String getUploadsSubPath(String subDir) {
        initialize();
        String subPath = uploadsPath + File.separator + subDir;
        File dir = new File(subPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return subPath;
    }
    
    /**
     * 🔍 Tìm file trong uploads với tên gần giống
     */
    public static File findSimilarFile(String searchName) {
        initialize();
        File uploadsDir = new File(uploadsPath);
        
        if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
            return null;
        }
        
        File[] files = uploadsDir.listFiles();
        if (files == null) {
            return null;
        }
        
        String normalizedSearch = normalizeFileName(searchName);
        File bestMatch = null;
        double bestScore = 0.0;
        
        for (File file : files) {
            if (file.isFile()) {
                String normalizedFile = normalizeFileName(file.getName());
                double similarity = calculateSimilarity(normalizedSearch, normalizedFile);
                
                if (similarity > bestScore && similarity > 0.6) {
                    bestScore = similarity;
                    bestMatch = file;
                }
            }
        }
        
        if (bestMatch != null) {
            logger.info("🎯 Found similar file: " + bestMatch.getName() + " (score: " + bestScore + ")");
        }
        
        return bestMatch;
    }
    
    /**
     * 🧹 Normalize file name cho việc so sánh
     */
    private static String normalizeFileName(String fileName) {
        if (fileName == null) return "";
        
        return fileName.toLowerCase()
                      .replaceAll("[^a-z0-9\\s]", "") // Chỉ giữ chữ, số và space
                      .replaceAll("\\s+", " ")       // Normalize space
                      .trim();
    }
    
    /**
     * 🧮 Tính độ tương tự giữa hai string
     */
    private static double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLen;
    }
    
    /**
     * 📏 Tính Levenshtein distance
     */
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * 📊 Lấy thông tin debug về paths
     */
    public static String getDebugInfo() {
        initialize();
        StringBuilder info = new StringBuilder();
        info.append("🗂️ PathManager Debug Info:\n");
        info.append("- Project Root: ").append(projectRoot).append("\n");
        info.append("- Uploads Path: ").append(uploadsPath).append("\n");
        info.append("- Current Dir: ").append(System.getProperty("user.dir")).append("\n");
        info.append("- User Home: ").append(System.getProperty("user.home")).append("\n");
        info.append("- Uploads Exists: ").append(new File(uploadsPath).exists()).append("\n");
        
        // Check if current path is correct
        String expectedPath = "D:\\Code\\dev files\\Java\\Netbeans\\EbookWebsite\\uploads";
        boolean isCorrectPath = uploadsPath != null && uploadsPath.equals(expectedPath);
        info.append("- Is Correct Path: ").append(isCorrectPath ? "✅ YES" : "❌ NO").append("\n");
        if (!isCorrectPath) {
            info.append("- Expected Path: ").append(expectedPath).append("\n");
        }
        
        File uploadsDir = new File(uploadsPath);
        if (uploadsDir.exists() && uploadsDir.isDirectory()) {
            File[] files = uploadsDir.listFiles();
            info.append("- Files Count: ").append(files != null ? files.length : 0).append("\n");
            
            if (files != null && files.length > 0) {
                info.append("- Sample Files:\n");
                for (int i = 0; i < Math.min(3, files.length); i++) {
                    info.append("  • ").append(files[i].getName()).append("\n");
                }
            }
        }
        
        return info.toString();
    }
} 