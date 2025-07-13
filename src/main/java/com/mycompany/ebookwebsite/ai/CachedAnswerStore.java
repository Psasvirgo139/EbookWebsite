package com.mycompany.ebookwebsite.ai;

import java.util.concurrent.ConcurrentHashMap;

public class CachedAnswerStore {
    private static final ConcurrentHashMap<String, String> answerCache = new ConcurrentHashMap<>();

    public static String get(String question) {
        return answerCache.get(question);
    }

    public static void put(String question, String answer) {
        answerCache.put(question, answer);
    }
} 