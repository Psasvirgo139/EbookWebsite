package com.mycompany.ebookwebsite.ai;

import java.util.concurrent.ConcurrentHashMap;

public class EmbeddingCache {
    public static final ConcurrentHashMap<String, float[]> cache = new ConcurrentHashMap<>();

    public static float[] getOrCompute(String question) {
        return cache.computeIfAbsent(question, EmbeddingUtil::getEmbedding);
    }
} 