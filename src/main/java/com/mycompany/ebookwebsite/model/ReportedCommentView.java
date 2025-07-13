package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class ReportedCommentView {
    private int id;
    private int commentId;
    private int reporterId;
    private String reporterUsername;
    private LocalDateTime reportedAt;
    private String reason;
    private String status;
    private String commentContent;
    private int commentUserId;
    private String commentUsername;

    public ReportedCommentView(int id, int commentId, int reporterId, String reporterUsername, LocalDateTime reportedAt, String reason, String status, String commentContent, int commentUserId, String commentUsername) {
        this.id = id;
        this.commentId = commentId;
        this.reporterId = reporterId;
        this.reporterUsername = reporterUsername;
        this.reportedAt = reportedAt;
        this.reason = reason;
        this.status = status;
        this.commentContent = commentContent;
        this.commentUserId = commentUserId;
        this.commentUsername = commentUsername;
    }

    public int getId() { return id; }
    public int getCommentId() { return commentId; }
    public int getReporterId() { return reporterId; }
    public String getReporterUsername() { return reporterUsername; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getCommentContent() { return commentContent; }
    public int getCommentUserId() { return commentUserId; }
    public String getCommentUsername() { return commentUsername; }
} 