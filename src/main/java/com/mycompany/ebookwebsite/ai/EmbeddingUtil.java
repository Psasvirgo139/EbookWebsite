package com.mycompany.ebookwebsite.ai;

import java.util.Random;

public class EmbeddingUtil {
    private static final Random random = new Random();
    
    // Tạm thời dùng hash-based embedding để tránh lỗi LangChain4j
    public static float[] getEmbedding(String text) {
        int hash = text.hashCode();
        float[] embedding = new float[1536]; // OpenAI embedding size
        random.setSeed(hash);
        for (int i = 0; i < embedding.length; i++) {
            embedding[i] = random.nextFloat() * 2 - 1; // -1 to 1
        }
        return embedding;
    }
} 