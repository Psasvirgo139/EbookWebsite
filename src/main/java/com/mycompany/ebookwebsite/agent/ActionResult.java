package com.mycompany.ebookwebsite.agent;

import java.util.HashMap;
import java.util.Map;

/**
 * ActionResult là kết quả trả về sau khi thực hiện một Action/tool.
 * Có thể chứa kết quả thành công (result) hoặc thông báo lỗi (error).
 */
public class ActionResult {
    private Object result;   // Kết quả trả về (dạng object, có thể là String, Map, List...)
    private String error;    // Lý do lỗi (nếu có)

    public ActionResult(Object result, String error) {
        this.result = result;
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    /**
     * Chuyển kết quả ra dạng Map (dùng khi cần serialize ra JSON hoặc truyền qua agent memory).
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (error == null) {
            map.put("result", result);
        } else {
            map.put("error", error);
        }
        return map;
    }
} 