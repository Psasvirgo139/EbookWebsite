package com.mycompany.ebookwebsite.agent;

/**
 * ModerationAgent là agent kiểm duyệt nội dung,
 * sử dụng AgentLoop để điều phối memory và gọi LLM kiểm tra nội dung.
 * Được thiết kế để có thể mở rộng thêm các chức năng kiểm duyệt khác.
 */
public class ModerationAgent {

    private final AgentLoop agentLoop;

    public ModerationAgent(LLM llm) {
        this.agentLoop = new AgentLoop(llm);
    }

    /**
     * Kiểm duyệt một nội dung text, trả về ActionResult gồm "OK" hoặc lý do vi phạm.
     * @param text Nội dung cần kiểm duyệt
     * @return ActionResult (result: "OK" nếu hợp lệ, error nếu vi phạm)
     */
    public ActionResult moderate(String text) {
        return agentLoop.moderate(text); // AgentLoop sẽ tự log flow!
    }

    /**
     * Xoá memory hội thoại của agent (nếu muốn reset)
     */
    public void reset() {
        agentLoop.resetMemory();
    }
} 