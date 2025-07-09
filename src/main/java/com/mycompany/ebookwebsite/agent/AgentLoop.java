package com.mycompany.ebookwebsite.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentLoop {

    private final LLM llm;
    private final List<Message> memory = new ArrayList<>();

    public AgentLoop(LLM llm) {
        this.llm = llm;
    }

    /**
     * Hàm moderate xử lý kiểm duyệt qua nhiều vòng (loop) nếu cần.
     */
    public ActionResult moderate(String text) {
        System.out.println("[AgentLoop] Bắt đầu agent loop với input: " + text); // LOG 1
        Message userMsg = new Message("user", text);
        memory.add(userMsg);

        // (Giả lập: gửi hết memory, có thể loop nhiều lần nếu cần)
        String llmResponse = llm.generateResponse(memory);
        System.out.println("[AgentLoop] LLM trả về: " + llmResponse); // LOG 2

        // ... xử lý response thành ActionResult ...
        ActionResult result;
        if (llmResponse != null && llmResponse.toLowerCase().contains("sex")) {
            result = new ActionResult(null, "Nội dung vi phạm: phát hiện từ cấm 'sex'");
        } else {
            result = new ActionResult("OK", null);
        }

        System.out.println("[AgentLoop] ActionResult trả về: " + result); // LOG 3
        return result;
    }

    /**
     * Reset toàn bộ memory của agent loop.
     */
    public void resetMemory() {
        memory.clear();
        System.out.println("[AgentLoop] Đã reset toàn bộ memory.");
    }
} 