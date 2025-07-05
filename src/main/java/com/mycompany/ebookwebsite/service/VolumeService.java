package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.VolumeDAO;
import com.mycompany.ebookwebsite.model.Volume;

import java.sql.SQLException;
import java.util.List;

public class VolumeService {
    private final VolumeDAO volumeDAO = new VolumeDAO();

    public List<Volume> getVolumesByEbookId(int ebookId) throws SQLException {
        return volumeDAO.selectVolumesByEbook(ebookId);
    }

    public Volume getVolumeById(int id) throws SQLException {
        return volumeDAO.selectVolume(id);
    }

    public List<Volume> getAllVolumes() throws SQLException {
        return volumeDAO.selectAllVolumes();
    }

    public void createVolume(Volume volume) throws SQLException {
        volumeDAO.insertVolume(volume);
    }

    public boolean updateVolume(Volume volume) throws SQLException {
        return volumeDAO.updateVolume(volume);
    }

    public boolean deleteVolume(int id) throws SQLException {
        return volumeDAO.deleteVolume(id);
    }

    public void incrementViewCount(int id) throws SQLException {
        volumeDAO.incrementViewCount(id);
    }

    public void incrementLikeCount(int id) throws SQLException {
        volumeDAO.incrementLikeCount(id);
    }

    public void decrementLikeCount(int id) throws SQLException {
        volumeDAO.decrementLikeCount(id);
    }
} 