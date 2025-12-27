package com.example.application.ClientManagementServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseAccess {

    private Connection open() throws SQLException {
        String url = "jdbc:mysql://sql.yamazaki.se.shibaura-it.ac.jp:13308/db_group_b";
        String user = "group_b";
        String pass = "group_b";
        return DriverManager.getConnection(url, user, pass);
    }

    public User getUserByUsername(String username) {
        final String sql = "SELECT id, username, password FROM account WHERE username = ?";
        try (Connection con = open();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("password"));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DB getUserByUsername failed", e);
        }
    }

    public boolean getLoginStatusByUsername(String username) {
        final String sql = "SELECT login_state FROM account WHERE username = ?";
        try (Connection con = open();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("login_state");
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DB getLoginStatusByUsername failed", e);
        }
    }
public RankRecord getRankRecordByUsername(String username) {
        // accountテーブルからランク情報を取得するクエリ
        final String sql = "SELECT rank1, rank2, rank3, rank4 FROM account WHERE username = ?";
        
        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 4つの順位カウントを読み取ってRecordを返す
                    return new RankRecord(
                        rs.getInt("rank1"),
                        rs.getInt("rank2"),
                        rs.getInt("rank3"),
                        rs.getInt("rank4")
                    );
                } else {
                    return new RankRecord(0, 0, 0, 0);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("DB getRankRecordByUsername failed", e);
        }
    }

    // 前の手順で追加した更新用メソッド
    public boolean incrementRankCount(String username, int rankNum) {
        String columnName = "rank" + rankNum;
        final String sql = "UPDATE account SET " + columnName + " = " + columnName + " + 1 WHERE username = ?";
        try (Connection con = open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("DB incrementRankCount failed", e);
        }
    }
    public User createUser(String username, String password, String id) {
        final String sql = "INSERT INTO account (id, username, password) VALUES (?, ?, ?)";
        try (Connection con = open();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, username);
            ps.setString(3, password);

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("Creating user failed, insert affected rows=" + rows);
            }
            return new User(id, username, password);
        } catch (Exception e) {
            throw new RuntimeException("DB createUser failed", e);
        }
    }

    public boolean setLoginStatus(String username, boolean loginState) {
        final String sql = "UPDATE account SET login_state = ? WHERE username = ?";
        try (Connection con = open();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, loginState);
            ps.setString(2, username);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("DB setLoggedIn failed", e);
        }
    }
}
