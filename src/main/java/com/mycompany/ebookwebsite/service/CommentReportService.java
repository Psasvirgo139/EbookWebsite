package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentReportDAO;
import com.mycompany.ebookwebsite.model.ReportedCommentView;
import java.sql.SQLException;
import java.util.List;

public class CommentReportService {
    private final CommentReportDAO reportDAO = new CommentReportDAO();

    public void reportComment(int commentId, int reporterId, String reason) throws SQLException {
        reportDAO.insertReport(commentId, reporterId, reason);
    }

    public List<ReportedCommentView> getAllReports() throws SQLException {
        return reportDAO.getAllReports();
    }

    public void updateReportStatus(int reportId, String status) throws SQLException {
        reportDAO.updateStatus(reportId, status);
    }
} 