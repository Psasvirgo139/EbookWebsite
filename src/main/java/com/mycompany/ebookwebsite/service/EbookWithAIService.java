package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.ebookwebsite.dao.EbookAIDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.EbookAI;

/**
 * Service để combine Ebook và EbookAI data
 * Cung cấp backward compatibility cho code cũ
 * 
 * @author ADMIN
 */
public class EbookWithAIService {
    
    private final EbookDAO ebookDAO;
    private final EbookAIDAO ebookAIDAO;
    
    public EbookWithAIService() {
        this.ebookDAO = new EbookDAO();
        this.ebookAIDAO = new EbookAIDAO();
    }
    
    /**
     * Extended Ebook class với AI capabilities
     */
    public static class EbookWithAI extends Ebook {
        private EbookAI aiData;
        
        public EbookWithAI(Ebook ebook, EbookAI aiData) {
            super(ebook.getId(), ebook.getTitle(), ebook.getDescription(), 
                  ebook.getReleaseType(), ebook.getLanguage(), ebook.getStatus(),
                  ebook.getVisibility(), ebook.getUploaderId(), ebook.getCreatedAt(),
                  ebook.getViewCount(), ebook.getCoverUrl());
            this.aiData = aiData;
        }
        
        public EbookWithAI(Ebook ebook) {
            this(ebook, null);
        }
        
        // ===== AI COMPATIBILITY METHODS =====
        
        public String getSummary() {
            return aiData != null ? aiData.getSummary() : null;
        }
        
        public void setSummary(String summary) {
            if (aiData == null) {
                aiData = new EbookAI();
                aiData.setEbookId(this.getId());
            }
            aiData.setSummary(summary);
        }
        
        public String getFileName() {
            return aiData != null ? aiData.getFileName() : null;
        }
        
        public void setFileName(String fileName) {
            if (aiData == null) {
                aiData = new EbookAI();
                aiData.setEbookId(this.getId());
            }
            aiData.setFileName(fileName);
        }
        
        public String getOriginalFileName() {
            return aiData != null ? aiData.getOriginalFileName() : null;
        }
        
        public void setOriginalFileName(String originalFileName) {
            if (aiData == null) {
                aiData = new EbookAI();
                aiData.setEbookId(this.getId());
            }
            aiData.setOriginalFileName(originalFileName);
        }
        
        public EbookAI getAiData() {
            return aiData;
        }
        
        public void setAiData(EbookAI aiData) {
            this.aiData = aiData;
        }
        
        @Override
        public String getCoverUrl() {
            String coverUrl = super.getCoverUrl();
            if (coverUrl != null && !coverUrl.trim().isEmpty()) {
                return coverUrl;
            }
            // Lấy coverUrl từ aiData nếu có
            if (aiData != null) {
                String aiCoverUrl = aiData.getCoverUrl();
                if (aiCoverUrl != null && !aiCoverUrl.trim().isEmpty()) {
                    return aiCoverUrl;
                }
            }
            // Fallback: đúng công thức image/<title>_cover.jpg
            return "image/" + getTitle() + "_cover.jpg";
        }
        
        @Override
        public String toString() {
            return super.toString() + " + AI{" + 
                   "summary=" + (aiData != null && aiData.getSummary() != null ? 
                                aiData.getSummary().substring(0, Math.min(50, aiData.getSummary().length())) + "..." : "null") + 
                   ", fileName=" + (aiData != null ? aiData.getFileName() : "null") + 
                   "}";
        }
    }
    
    /**
     * Lấy ebook với AI data
     */
    public EbookWithAI getEbookWithAI(int ebookId) throws SQLException {
        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) {
            return null;
        }
        
        EbookAI aiData = ebookAIDAO.getByEbookId(ebookId);
        return new EbookWithAI(ebook, aiData);
    }
    
    /**
     * Cập nhật summary cho ebook
     */
    public boolean updateSummary(int ebookId, String summary) throws SQLException {
        // Kiểm tra ebook có tồn tại không
        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) {
            return false;
        }
        
        // Upsert AI data
        EbookAI aiData = ebookAIDAO.getByEbookId(ebookId);
        if (aiData == null) {
            aiData = new EbookAI(ebookId, null, null, summary);
            ebookAIDAO.insertEbookAI(aiData);
        } else {
            aiData.setSummary(summary);
            ebookAIDAO.updateEbookAI(aiData);
        }
        
        return true;
    }
    
    /**
     * Cập nhật file info cho ebook
     */
    public boolean updateFileInfo(int ebookId, String fileName, String originalFileName) throws SQLException {
        // Kiểm tra ebook có tồn tại không
        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) {
            return false;
        }
        
        // Upsert AI data
        EbookAI aiData = ebookAIDAO.getByEbookId(ebookId);
        if (aiData == null) {
            aiData = new EbookAI(ebookId, fileName, originalFileName, null);
            ebookAIDAO.insertEbookAI(aiData);
        } else {
            aiData.setFileName(fileName);
            aiData.setOriginalFileName(originalFileName);
            ebookAIDAO.updateEbookAI(aiData);
        }
        
        return true;
    }
    
    /**
     * Cập nhật Ebook và AI data
     */
    public boolean updateEbookWithAI(EbookWithAI ebookWithAI) throws SQLException {
        // Update core ebook data
        boolean ebookUpdated = ebookDAO.updateEbook(ebookWithAI);
        
        // Update AI data if present
        if (ebookWithAI.getAiData() != null) {
            EbookAI aiData = ebookWithAI.getAiData();
            aiData.setEbookId(ebookWithAI.getId());
            ebookAIDAO.upsertEbookAI(aiData);
        }
        
        return ebookUpdated;
    }
    
    // ===== DELEGATION METHODS FOR BACKWARD COMPATIBILITY =====
    
    public Ebook getBookById(int id) throws SQLException {
        return ebookDAO.getBookById(id);
    }
    
    public List<Ebook> getBooksByPage(int offset, int limit) throws SQLException {
        return ebookDAO.getBooksByPage(offset, limit);
    }
    
    public int countAllBooks() throws SQLException {
        return ebookDAO.countAllBooks();
    }
    
    public void incrementViewCount(int id) throws SQLException {
        ebookDAO.incrementViewCount(id);
    }
} 