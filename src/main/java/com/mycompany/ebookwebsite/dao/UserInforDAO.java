package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.UserInfor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInforDAO {
    
    private static final String INSERT = "INSERT INTO UserInfor (phone, birth_day, gender, address, introduction) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM UserInfor";
    private static final String SELECT_BY_ID = "SELECT * FROM UserInfor WHERE id = ?";
    private static final String UPDATE = "UPDATE UserInfor SET phone = ?, birth_day = ?, gender = ?, address = ?, introduction = ? WHERE id = ?";
    private static final String DELETE = "UPDATE UserInfor SET status = 'deleted' WHERE id = ?";
    private static final String SELECT_ACTIVE = "SELECT * FROM UserInfor WHERE status != 'deleted' OR status IS NULL";

    public void insertUserInfor(UserInfor userInfor) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, userInfor.getPhone());
            
            if (userInfor.getBirthDay() != null) {
                ps.setDate(2, Date.valueOf(userInfor.getBirthDay()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            
            ps.setString(3, userInfor.getGender());
            ps.setString(4, userInfor.getAddress());
            ps.setString(5, userInfor.getIntroduction());
            
            ps.executeUpdate();
            
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                userInfor.setId(generatedKeys.getInt(1));
            }
        }
    }

    public UserInfor selectUserInfor(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUserInfor(rs);
            }
        }
        return null;
    }

    public List<UserInfor> selectAllUserInfor() throws SQLException {
        List<UserInfor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUserInfor(rs));
            }
        }
        return list;
    }

    public List<UserInfor> selectActiveUserInfor() throws SQLException {
        List<UserInfor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ACTIVE)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUserInfor(rs));
            }
        }
        return list;
    }

    public boolean deleteUserInfor(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUserInfor(UserInfor userInfor) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, userInfor.getPhone());
            
            if (userInfor.getBirthDay() != null) {
                ps.setDate(2, Date.valueOf(userInfor.getBirthDay()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            
            ps.setString(3, userInfor.getGender());
            ps.setString(4, userInfor.getAddress());
            ps.setString(5, userInfor.getIntroduction());
            ps.setInt(6, userInfor.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    private UserInfor mapUserInfor(ResultSet rs) throws SQLException {
        return new UserInfor(
                rs.getInt("id"),
                rs.getString("phone"),
                (rs.getDate("birth_day") != null) ? rs.getDate("birth_day").toLocalDate() : null,
                rs.getString("gender"),
                rs.getString("address"),
                rs.getString("introduction")
        );
    }
}