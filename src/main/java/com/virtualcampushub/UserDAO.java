package com.virtualcampushub;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDAO {

    // ─────────────────────────────────────────
    // AUTHENTIFICATION
    // ─────────────────────────────────────────

    public static boolean registerUser(String username, String email, String password) {
        if (emailExists(email) || usernameExists(username)) return false;

        String sql = "INSERT INTO users (username, email, password, full_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashPassword(password));
            stmt.setString(4, username);
            stmt.executeUpdate();
            System.out.println("✅ Utilisateur enregistré : " + username);
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Erreur inscription : " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return storedHash.equals(hashPassword(password));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur connexion : " + e.getMessage());
        }
        return false;
    }

    public static String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("username");

        } catch (SQLException e) {
            System.err.println("❌ Erreur getUsernameByEmail : " + e.getMessage());
        }
        return "Utilisateur";
    }

    public static int getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            System.err.println("❌ Erreur getUserId : " + e.getMessage());
        }
        return -1;
    }

    // ─────────────────────────────────────────
    // VÉRIFICATIONS
    // ─────────────────────────────────────────

    public static boolean emailExists(String email) {
        return checkExists("SELECT COUNT(*) FROM users WHERE email = ?", email);
    }

    public static boolean usernameExists(String username) {
        return checkExists("SELECT COUNT(*) FROM users WHERE username = ?", username);
    }

    private static boolean checkExists(String sql, String value) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur checkExists : " + e.getMessage());
        }
        return false;
    }

    // ─────────────────────────────────────────
    // SÉCURITÉ - HASH SHA-256
    // ─────────────────────────────────────────

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 introuvable", e);
        }
    }

    // ─────────────────────────────────────────
    // MISE À JOUR PROFIL
    // ─────────────────────────────────────────

    public static boolean updateProfile(int userId, String fullName, String email) {
        String sql = "UPDATE users SET full_name = ?, email = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur updateProfile : " + e.getMessage());
            return false;
        }
    }
}
