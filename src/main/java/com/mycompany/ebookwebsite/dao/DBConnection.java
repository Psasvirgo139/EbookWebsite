/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.utils.Utils;

/**
 *
 * @author ADMIN
 */
public class DBConnection {
    public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String dbURL = Utils.getEnv("DB_URL") != null ? 
        Utils.getEnv("DB_URL") : "jdbc:sqlserver://localhost:1433;databaseName=EBookWebsite;encrypt=true;trustServerCertificate=true";
    public static String userDB = Utils.getEnv("DB_USER") != null ? 
        Utils.getEnv("DB_USER") : "sa";
    public static String passDB = Utils.getEnv("DB_PASSWORD") != null ? 
        Utils.getEnv("DB_PASSWORD") : "123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(dbURL, userDB, passDB);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Database driver not found", ex);
        }
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("Connect to EBookWebsite Success");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}