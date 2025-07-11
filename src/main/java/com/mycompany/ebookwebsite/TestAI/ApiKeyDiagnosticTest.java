package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 🔑 API Key Diagnostic Test - Chẩn đoán toàn diện vấn đề API key
 * 
 * Kiểm tra tất cả các nguồn có thể có của API key và .env file loading
 */
public class ApiKeyDiagnosticTest {
    
    public static void main(String[] args) {
        System.out.println("🔑 API KEY DIAGNOSTIC TEST");
        System.out.println("==========================");
        
        // Test 1: Kiểm tra .env file existence
        checkEnvFileExistence();
        
        // Test 2: Kiểm tra Utils.getEnv()
        checkUtilsGetEnv();
        
        // Test 3: Kiểm tra các nguồn environment variables
        checkEnvironmentSources();
        
        // Test 4: Kiểm tra dotenv library
        checkDotenvLibrary();
        
        // Test 5: Manual .env file reading
        manualEnvFileRead();
        
        // Test 6: Tạo .env file mẫu nếu không tồn tại
        createSampleEnvFile();
        
        System.out.println("\n✅ API Key Diagnostic Completed!");
    }
    
    /**
     * 1. Kiểm tra .env file có tồn tại không
     */
    private static void checkEnvFileExistence() {
        System.out.println("\n1. 📂 .ENV FILE EXISTENCE CHECK:");
        
        String[] envPaths = {
            ".env",
            "D:\\Code\\dev files\\Java\\Netbeans\\EbookWebsite\\.env",
            System.getProperty("user.dir") + File.separator + ".env",
            System.getProperty("user.dir") + File.separator + "src" + File.separator + ".env"
        };
        
        for (String path : envPaths) {
            File envFile = new File(path);
            System.out.println("📁 Checking: " + path);
            System.out.println("   Exists: " + envFile.exists());
            System.out.println("   Can Read: " + envFile.canRead());
            System.out.println("   Size: " + (envFile.exists() ? envFile.length() + " bytes" : "N/A"));
            
            if (envFile.exists()) {
                System.out.println("   ✅ FOUND .env file at: " + envFile.getAbsolutePath());
            }
            System.out.println();
        }
    }
    
    /**
     * 2. Kiểm tra Utils.getEnv()
     */
    private static void checkUtilsGetEnv() {
        System.out.println("2. 🔧 UTILS.GETENV() CHECK:");
        
        try {
            String apiKey = Utils.getEnv("OPENAI_API_KEY");
            System.out.println("Utils.getEnv('OPENAI_API_KEY'): " + maskApiKey(apiKey));
            
            if (apiKey != null) {
                if (apiKey.startsWith("test-api")) {
                    System.out.println("❌ Using DEFAULT TEST KEY - This causes the error!");
                } else if (apiKey.startsWith("sk-")) {
                    System.out.println("✅ Real OpenAI API key detected");
                } else {
                    System.out.println("❓ Unknown API key format");
                }
            } else {
                System.out.println("❌ API key is NULL");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error in Utils.getEnv(): " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * 3. Kiểm tra các nguồn environment variables
     */
    private static void checkEnvironmentSources() {
        System.out.println("3. 🌍 ENVIRONMENT SOURCES CHECK:");
        
        // System Property
        String sysProp = System.getProperty("OPENAI_API_KEY");
        System.out.println("System.getProperty('OPENAI_API_KEY'): " + maskApiKey(sysProp));
        
        // Environment Variable
        String envVar = System.getenv("OPENAI_API_KEY");
        System.out.println("System.getenv('OPENAI_API_KEY'): " + maskApiKey(envVar));
        
        // All environment variables
        System.out.println("\nAll environment variables containing 'API':");
        System.getenv().entrySet().stream()
            .filter(entry -> entry.getKey().toUpperCase().contains("API"))
            .forEach(entry -> System.out.println("  " + entry.getKey() + " = " + maskApiKey(entry.getValue())));
        
        System.out.println();
    }
    
    /**
     * 4. Kiểm tra dotenv library
     */
    private static void checkDotenvLibrary() {
        System.out.println("4. 📚 DOTENV LIBRARY CHECK:");
        
        try {
            Class<?> dotenvClass = Class.forName("io.github.cdimascio.dotenv.Dotenv");
            System.out.println("✅ Dotenv library found: " + dotenvClass.getName());
            
            // Try to create Dotenv instance
            Object dotenvBuilder = dotenvClass.getMethod("configure").invoke(null);
            Object dotenv = dotenvBuilder.getClass().getMethod("ignoreIfMissing").invoke(dotenvBuilder);
            dotenv = dotenv.getClass().getMethod("load").invoke(dotenv);
            
            String apiKey = (String) dotenv.getClass().getMethod("get", String.class)
                .invoke(dotenv, "OPENAI_API_KEY");
            
            System.out.println("Dotenv.get('OPENAI_API_KEY'): " + maskApiKey(apiKey));
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Dotenv library NOT found in classpath");
            System.out.println("   This means .env files won't be automatically loaded!");
        } catch (Exception e) {
            System.out.println("❌ Error using Dotenv library: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 5. Manual .env file reading
     */
    private static void manualEnvFileRead() {
        System.out.println("5. 📖 MANUAL .ENV FILE READING:");
        
        String[] envPaths = {
            ".env",
            "D:\\Code\\dev files\\Java\\Netbeans\\EbookWebsite\\.env"
        };
        
        for (String path : envPaths) {
            File envFile = new File(path);
            if (envFile.exists()) {
                System.out.println("📁 Reading: " + envFile.getAbsolutePath());
                try {
                    Properties props = new Properties();
                    try (FileInputStream fis = new FileInputStream(envFile);
                         InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                        
                        // Read line by line
                        java.io.BufferedReader bufferedReader = new java.io.BufferedReader(reader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            line = line.trim();
                            if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                                String[] parts = line.split("=", 2);
                                if (parts.length == 2) {
                                    String key = parts[0].trim();
                                    String value = parts[1].trim();
                                    props.setProperty(key, value);
                                    
                                    if (key.equals("OPENAI_API_KEY")) {
                                        System.out.println("   ✅ Found OPENAI_API_KEY: " + maskApiKey(value));
                                    }
                                }
                            }
                        }
                    }
                    
                    System.out.println("   Properties loaded: " + props.size());
                    
                } catch (Exception e) {
                    System.out.println("   ❌ Error reading file: " + e.getMessage());
                }
            } else {
                System.out.println("📁 Not found: " + path);
            }
        }
        System.out.println();
    }
    
    /**
     * 6. Tạo .env file mẫu
     */
    private static void createSampleEnvFile() {
        System.out.println("6. 📝 CREATE SAMPLE .ENV FILE:");
        
        File envFile = new File(".env");
        if (!envFile.exists()) {
            try {
                String content = "# OpenAI Configuration\n" +
                               "OPENAI_API_KEY=sk-your-actual-openai-api-key-here\n" +
                               "\n" +
                               "# Database Configuration (optional)\n" +
                               "DB_USER=your_db_user\n" +
                               "DB_PASSWORD=your_db_password\n" +
                               "\n" +
                               "# Upload folder (optional)\n" +
                               "UPLOAD_FOLDER=uploads/\n";
                
                java.nio.file.Files.write(envFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
                System.out.println("✅ Created sample .env file: " + envFile.getAbsolutePath());
                System.out.println("   Please edit this file and add your real OpenAI API key");
                
            } catch (Exception e) {
                System.out.println("❌ Error creating .env file: " + e.getMessage());
            }
        } else {
            System.out.println("✅ .env file already exists: " + envFile.getAbsolutePath());
        }
        System.out.println();
    }
    
    /**
     * Mask API key for security
     */
    private static String maskApiKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return "NULL";
        }
        
        if (key.length() < 8) {
            return "*".repeat(key.length());
        }
        
        if (key.length() <= 16) {
            return key.substring(0, 4) + "*".repeat(key.length() - 8) + key.substring(key.length() - 4);
        } else {
            return key.substring(0, 8) + "*".repeat(8) + key.substring(key.length() - 4);
        }
    }
} 