package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentVoteDAO;
import com.mycompany.ebookwebsite.model.CommentVote;
import java.util.Optional;

public class CommentVoteService {
    private final CommentVoteDAO voteDAO = new CommentVoteDAO();

    public boolean like(int userId, int commentId) {
        return voteDAO.upsertVote(userId, commentId, 1);
    }

    public boolean dislike(int userId, int commentId) {
        return voteDAO.upsertVote(userId, commentId, -1);
    }

    public boolean removeVote(int userId, int commentId) {
        return voteDAO.deleteVote(userId, commentId);
    }

    public Optional<CommentVote> getVote(int userId, int commentId) {
        return voteDAO.getVote(userId, commentId);
    }

    public int getLikeCount(int commentId) {
        return voteDAO.countVotes(commentId, 1);
    }

    public int getDislikeCount(int commentId) {
        return voteDAO.countVotes(commentId, -1);
    }

    public boolean toggleDislike(int userId, int commentId) throws java.sql.SQLException {
        Integer current = getVoteValue(userId, commentId);
        if (current != null && current == -1) {
            // Đã dislike, bỏ dislike
            removeVote(userId, commentId);
            return false;
        } else {
            // Nếu đã like thì chuyển thành dislike
            setVote(userId, commentId, -1);
            return true;
        }
    }

    public Integer getVoteValue(int userId, int commentId) {
        Optional<CommentVote> vote = voteDAO.getVote(userId, commentId);
        return vote.map(CommentVote::getValue).orElse(null);
    }

    public void setVote(int userId, int commentId, int value) {
        voteDAO.upsertVote(userId, commentId, value);
    }

    public boolean toggleLike(int userId, int commentId) throws java.sql.SQLException {
        Integer current = getVoteValue(userId, commentId);
        if (current != null && current == 1) {
            // Đã like, bỏ like
            removeVote(userId, commentId);
            return false;
        } else {
            // Nếu đã dislike thì chuyển thành like
            setVote(userId, commentId, 1);
            return true;
        }
    }
} 