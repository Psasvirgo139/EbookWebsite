package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.utils.Utils;

public class QuickApiKeyTest {
    public static void main(String[] args) {
        System.out.println("🔍 Quick API Key Test:");
        String apiKey = Utils.getEnv("OPENAI_API_KEY");
        System.out.println("API Key result: " + (apiKey != null ? apiKey : "NULL"));
        
        if (apiKey == null) {
            System.out.println("✅ GOOD! No default test key returned");
            System.out.println("💡 Now you need to set real API key");
        } else if (apiKey.equals("test-api-key-for-demo")) {
            System.out.println("❌ BAD! Still returning test key");
        } else {
            System.out.println("✅ GOOD! Real API key detected");
        }
    }
} 