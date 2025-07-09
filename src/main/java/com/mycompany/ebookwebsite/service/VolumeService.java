package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.VolumeDAO;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.User;
import java.sql.SQLException;
import java.util.List;

public class VolumeService {
    private final VolumeDAO volumeDAO = new VolumeDAO();

    public void addVolume(Volume volume) throws SQLException {
        volumeDAO.insertVolume(volume);
    }
    public boolean updateVolume(Volume volume) throws SQLException {
        return volumeDAO.updateVolume(volume);
    }
    public boolean deleteVolume(int id, User user) throws SQLException {
        if (!"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa volume!");
        }
        return volumeDAO.deleteVolume(id);
    }
    public Volume getVolumeById(int id) throws SQLException {
        return volumeDAO.selectVolume(id);
    }
    public List<Volume> getVolumesByEbook(int ebookId) throws SQLException {
        return volumeDAO.selectVolumesByEbook(ebookId);
    }
    public List<Volume> searchVolumes(String title) throws SQLException {
        return volumeDAO.search(title);
    }
} 